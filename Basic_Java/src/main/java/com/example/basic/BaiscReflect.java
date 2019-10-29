package com.example.basic;

import java.lang.reflect.*;
import java.util.regex.*;
import  java.util.Arrays;
public class BaiscReflect{
	private static Pattern pattern=Pattern.compile("\\w+\\.");
	public static void main(String[] args) {
		if(args.length<1){
			System.out.println("no parameter will exit");
			System.exit(0);
		}
		try{
			Class<?> cls=Class.forName(args[0]);
			// one(cls);
			two(cls);
			// three(cls);
		}catch(Exception e){
			System.out.println(e.toString());
		}

	}

	//通过反射获取构造期
	private static void one(Class<?> cls) throws Exception{
			//获取无参构造期，如果没有会报NoSuchMethodException异常
			Constructor<?> c=cls.getDeclaredConstructor();
			Constructor<?> c2=cls.getDeclaredConstructor(int.class);

			//设置访问private的权限
			c.setAccessible(true);
			c2.setAccessible(true);

			//获取实例
			Object obj=c.newInstance();
			//如果参数不正确编译无错误，运行报java.lang.IllegalArgumentException
			Object obj2=c2.newInstance(10);
			// 不能访问private,没找到设置访问权限的方法
			Object obj3=cls.newInstance();

			if(obj instanceof  ShowMethod){
				ShowMethod method=(ShowMethod)obj;
			System.out.println(method.toString());
			ShowMethod method2=(ShowMethod)obj2;
			System.out.println(method2.toString());
			
			}
		

	}
	// 反射字段
	private static void two(Class<?> cls) throws Exception{
		Constructor s= cls.getDeclaredConstructor();
		s.setAccessible(true);
		Object obj=s.newInstance();

		Field f=cls.getDeclaredField("number");
		f.setAccessible(true);
		//如果字段是static的，obj可为null
		f.set(obj,12);
		Field f2=cls.getDeclaredField("count");
		f2.setAccessible(true);
		//设置无效，未改变字段的值，如果是static final 报异常
		f2.set(obj,32);

		Field f3=cls.getDeclaredField("content");
		f3.setAccessible(true);
		String content=(String)f3.get(obj);
		String newContent=content.replace('o','b');
		f3.set(obj,newContent);
		//字段为数组类型
		Field f4=cls.getDeclaredField("length");
		f4.setAccessible(true);
		String name=f4.getType().getName();
		System.out.println("the type of field length is "+name);
		//获取该字段对象
		Object length=f4.get(obj);
		// ／／判断是否是数组类型
		if(length.getClass().isArray()){
			//如果是，获取长度
			int len=Array.getLength(length);
			//使用工具类打印
			System.out.println("print array "+Arrays.toString((int[])length));
			// 依次打印
			for(int i=0;i<len;i++){
				System.out.print(Array.get(length,i)+" ");
			}
			System.out.println();
		}
		
		System.out.println(obj);
	}

// 方法的反射
	private static void three(Class<?> cls)throws Exception{
		Constructor c=cls.getDeclaredConstructor();
		c.setAccessible(true);
		Object obj=c.newInstance();
		System.out.println(obj);

		Method m=cls.getDeclaredMethod("show");
		Method m2=cls.getDeclaredMethod("print",String.class);

		m.setAccessible(true);
		m2.setAccessible(true);

		m.invoke(obj);
		m2.invoke(obj,"hei hei hei");



	}
	private static void five(Class<?> cls){
		
		Constructor[] cons=cls.getConstructors();
		Method[] methods=cls.getMethods();
		Field[] fields=cls.getFields();
		for(Constructor con:cons){
			System.out.println(pattern.matcher(con.toString()).replaceAll(""));
		}	
		for(Method method:methods){
			System.out.println(pattern.matcher(method.toString()).replaceAll(""));
		}	
		for(Field field:fields){
			System.out.println(pattern.matcher(field.toString()).replaceAll(""));
		}
		
	}
}

class ShowMethod{
	private String content;
	private int number;
	private  final int count=20;
	private int[] length;
	private ShowMethod(){
		content="hello,every one";
		length=new int[]{1,2,3};
	}
	public ShowMethod(int a){
		number=a;
		System.out.println("a is"+a);
	}

	public String toString(){
		return "there is a number "+
		number+ "and count "+
		count+"the content is "+content;

	}
	private void show(){
		System.out.println("show youself");
	}

	private void print(String name){
		System.out.println("you get name "+name);
	}

}