package com.example.annotation;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import javax.lang.model.element.Modifier;
import javax.swing.filechooser.FileSystemView;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ExcelParse {


    private POIFSFileSystem mFileSystem;
    private int mLastRowNum;
    Path path = Paths.get("C:\\Users\\Administrator\\Desktop\\");

    private HSSFSheet openExcelFile() throws IOException {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        String dir = fileSystemView.getHomeDirectory().getPath();
        FileInputStream fileInputStream = new FileInputStream(new File(dir, "design2.xls"));
        BufferedInputStream bis = new BufferedInputStream(fileInputStream);
        mFileSystem = new POIFSFileSystem(bis);
        HSSFWorkbook workbook = new HSSFWorkbook(mFileSystem);
        HSSFSheet sheet = workbook.getSheet("Sheet1");
        mLastRowNum = sheet.getLastRowNum();

        System.out.println("last row " + mLastRowNum);

        return sheet;
    }

    private void parse(HSSFSheet sheet) throws IOException {
        final int anchorColNum = 1;
        final String anchorContent = "功能";
//        Retrofit 的方法集合
        List<MethodSpec> methodSpecs = new ArrayList<>();

        for (int rowNum = 0; rowNum < mLastRowNum; rowNum++) {
//            获取行
            HSSFRow row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
//            获取指定列的单元格
            HSSFCell cell = row.getCell(anchorColNum);
            if (cell == null) {
                continue;
            }
//            单元格内容类型
            CellType cellTypeEnum = cell.getCellTypeEnum();
//            只处理string类型
            if (!(CellType.STRING == cellTypeEnum)) {
                continue;
            }
//            获取单元格内容，使用GBK
            String content = new String(cell.getStringCellValue().getBytes(), Charset.forName("gbk"));

            boolean result = Objects.equals(anchorContent, content);
            if (result) {
                //        功能单元格向下和向右偏移一个 获取请求路径
                String url = sheet.getRow(rowNum + 1).getCell(anchorColNum + 1).getStringCellValue();
//                System.out.println("path "+path);
                if (url.length() == 0) continue;
                String[] split = url.split("/");
//                最后一个单词作为retrofit service的方法名
                String lastWord = split[split.length - 1];
//                将路径最后一个单词首字母大写加Request 作为类名
                String name = String.valueOf(lastWord.charAt(0)).toUpperCase().concat(lastWord.substring(1, lastWord.length())).concat("Request");
//                创建实体
//                createEntity(sheet, rowNum, anchorColNum,name);

//                retrofit service的方法
                MethodSpec methodSpec = createMethodSpec(url, lastWord);
                methodSpecs.add(methodSpec);
                System.out.println();
            }
        }

        TypeSpec kimService = TypeSpec.interfaceBuilder("KimAscendService")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodSpecs)
                .build();
        JavaFile builder = JavaFile.builder("com.api", kimService).build();
        builder.writeTo(path);
//        builder.writeTo(System.out);

    }

/*
     * @param url
     * @param lastWord
     * @return 生成结果
     * @POST("/user/SMSvalidate") Call<RequestBody> SMSvalidate(@Body RequestBody request);*/


    private MethodSpec createMethodSpec(String url, String lastWord) {
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        // 方法的注解，成员是键值对，键默认是“value”
        AnnotationSpec annotationSpec = AnnotationSpec.builder(POST.class)
                .addMember("value", "$S", url)
                .build();
//        方法参数的注解
        ParameterSpec mothodParam = ParameterSpec.builder(RequestBody.class, "request").addAnnotation(Body.class).build();
//    返回参数类型 泛型嵌泛型
        ParameterizedTypeName typeName = ParameterizedTypeName.get(Response.class, Login.class);
        ParameterizedTypeName typeName2 = ParameterizedTypeName.get(ClassName.get(LiveData.class),typeName);
        return MethodSpec.methodBuilder(lastWord)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(annotationSpec)
                .addParameter(mothodParam)
                .returns(typeName2)
                .build();

    }

    Pattern PATTERN_CHINESE = Pattern.compile("[\\u4E00-\\u9FBF]+");
    Pattern PATTERN_ENGLISH = Pattern.compile("[a-zA-Z0-9]+");
    //    字段是英文，有数字也可以
    String MATCH_ENGLISH = "[a-zA-Z0-9]+";

    //当我们定位到功能这个单元格,开始创建实体类
    private void createEntity(HSSFSheet sheet, int rowNum, int anchorCol, String name) throws IOException {
        int offset = rowNum + 3;
        HashMap<String, TypeName> map = new HashMap<>();
        String cell = sheet.getRow(offset).getCell(anchorCol).getStringCellValue();
//        如果是英文
        while (cell.matches(MATCH_ENGLISH)) {
//            System.out.println("field " + cell);
            String type = sheet.getRow(offset).getCell(anchorCol + 2).getStringCellValue().toLowerCase();
            switch (type) {
                case "string":
                    map.put(cell, TypeName.get(String.class));
                    break;
                case "int":
                    map.put(cell, TypeName.INT);
                    break;
            }
            offset++;
            cell = sheet.getRow(offset).getCell(anchorCol).getStringCellValue();
        }

        if (map.size() < 2) return;
        //类构造器
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC);
//        构造函数构造器
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
        constructorBuilder.addModifiers(Modifier.PUBLIC);

        Set<Map.Entry<String, TypeName>> entries = map.entrySet();
        for (Map.Entry<String, TypeName> entry : entries) {
            FieldSpec fieldSpec = FieldSpec.builder(entry.getValue(), entry.getKey())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .build();

            classBuilder.addField(fieldSpec);

            constructorBuilder
                    .addParameter(entry.getValue(), entry.getKey())
                    .addStatement("this.$N = $N", entry.getKey(), entry.getKey());
        }

        classBuilder.addMethod(constructorBuilder.build());


        JavaFile javaFile = JavaFile.builder("com.generate", classBuilder.build()).build();
        javaFile.writeTo(path);
//        javaFile.writeTo(System.out);


    }

public static class LiveData{

}
public static class Login{

}
    public static void main(String[] args) {

        ExcelParse excelParse = new ExcelParse();
        try {
            HSSFSheet sheet = excelParse.openExcelFile();
            excelParse.parse(sheet);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }

    }
}
