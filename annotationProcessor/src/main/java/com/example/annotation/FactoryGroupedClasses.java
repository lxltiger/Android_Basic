package com.example.annotation;

import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

/**
 * Created by  on 2017/3/18
 * qq:1220289215
 */

public class FactoryGroupedClasses {
    private String mQulifiedClassName;
    /**
     * Will be added to the name of the generated factory class
     */
    private static final String SUFFIX = "Factory";

    private Map<String, FactoryAnnotateClass> itemsMap = new LinkedHashMap<>();
    public FactoryGroupedClasses(String qulifiedClassName) {
        mQulifiedClassName = qulifiedClassName;
    }

    public void add(FactoryAnnotateClass factoryAnnotateClass)throws IdAlreadyExistException  {
        FactoryAnnotateClass existing = itemsMap.get(factoryAnnotateClass.getId());
        if (existing!=null) {
            throw new IdAlreadyExistException(existing);
        }
        itemsMap.put(factoryAnnotateClass.getId(), factoryAnnotateClass);
    }

    public void generateCode(Elements elementUtils, Filer filer)throws IOException {
        TypeElement superClassName = elementUtils.getTypeElement(mQulifiedClassName);
        String factoryClassName = superClassName.getSimpleName() + SUFFIX;

        JavaFileObject jfo = filer.createSourceFile(mQulifiedClassName + SUFFIX);
        Writer writer = jfo.openWriter();
        JavaWriter jw = new JavaWriter(writer);

        // Write package
        PackageElement pkg = elementUtils.getPackageOf(superClassName);
        if (!pkg.isUnnamed()) {
            jw.emitPackage(pkg.getQualifiedName().toString());
            jw.emitEmptyLine();
        } else {
            jw.emitPackage("");
        }

        jw.beginType(factoryClassName, "class", EnumSet.of(Modifier.PUBLIC));
        jw.emitEmptyLine();
        jw.beginMethod(mQulifiedClassName, "create", EnumSet.of(Modifier.PUBLIC), "String", "id");

        jw.beginControlFlow("if (id == null)");
        jw.emitStatement("throw new IllegalArgumentException(\"id is null!\")");
        jw.endControlFlow();

        for (FactoryAnnotateClass item : itemsMap.values()) {
            jw.beginControlFlow("if (\"%s\".equals(id))", item.getId());
            jw.emitStatement("return new %s()", item.getAnnotatedTypeElement().getQualifiedName().toString());
            jw.endControlFlow();
            jw.emitEmptyLine();
        }

        jw.emitStatement("throw new IllegalArgumentException(\"Unknown id = \" + id)");
        jw.endMethod();

        jw.endType();

        jw.close();
    }
}
