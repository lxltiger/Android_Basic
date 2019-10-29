package com.example.basic;

import java.util.*;
public class Type{
	public static Random rand=new Random();
	public static void main(String[] args) {
		Type type=new Type();
		// type.basic();
		// type.middle();
		// type.middle2();
		// type.middle3();
		type.middle4();




	}

	private void basic(){
		try{
			Class.forName("Candy");
		}catch(ClassNotFoundException e){
			System.out.println("not Candy class");
		}
	}
	private void middle(){
		try{
			Class candy=Class.forName("Candy");
			show(candy);
			for(Class f:candy.getInterfaces()){
				show(f);
			}
			Class up=candy.getSuperclass();
			//need defalut constructor
			Object obj=up.newInstance();
			show(obj.getClass());

		}catch(ClassNotFoundException e){
			System.out.println("not Candy class");
			System.exit(1);
		}catch(InstantiationException e)	{
			System.out.println("can not instantiation");
			System.exit(1);

		}catch(IllegalAccessException e){
			System.out.println(" illegal access");
			System.exit(1);

		}
	}

	private void show(Class cc){
		System.out.println("Class name "+cc.getName());
		System.out.println("is interface "+cc.isInterface());
		System.out.println("simple name "+cc.getSimpleName());
		System.out.println("canonicalName "+cc.getCanonicalName());
	}

	private void middle2(){
		//.class 获得类的应用不会引起初始化，Class.forName()会
		Class a=A.class;
		//不会引起A的初始化，即静态代码块不会执行
		System.out.println("number"+A.number);
		// 引起A的初始化
		System.out.println("number2"+A.number2);
		//static 而非final会引起链接和初始化
		System.out.println("candy not final"+Candy.nonFinal);

	}
	private void middle3(){
		Class<?> one=int.class;
		one=double.class;
		one=A.class;
		Class<? extends Number> something=int.class;
		something=float.class;
		something=Number.class;


	}
	private void middle4(){
		FillList<Sweat> fillList=new FillList<Sweat>(Sweat.class);
		try{
			List<Sweat> sweatList=fillList.create(10);
			System.out.println(sweatList);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}

class A{
	//编译期常量，不需初始化A，即可使用
	static final int number=23;
	// 非编译器常量，会强制类初始化
	static final int number2=Type.rand.nextInt(100);
	static{

		System.out.println("a initialization");
	}
}
interface B{

}
class Sweat{
	private final int nunber=count++;
	private static  int count=0;
	public Sweat(){}
	public Sweat(int number){

	}
	public String toString(){
		return nunber+"";

	}
}
class Candy extends Sweat implements B{
	static int nonFinal=32;
	public Candy(){this(4);}
	public Candy(int number){
		super(number);
	}
	static{
		System.out.println("here is Candy");
	}
}

class FillList<T>{
	Class<T> tclass;
	public FillList(Class<T> tclass){
		this.tclass=tclass;
	}

	public List<T> create(int size) throws Exception{
		List<T> list=new ArrayList<T>();
		for(int i=0;i<size;i++){
			T t=tclass.newInstance();
			list.add(t);
		}
		return list;
	}


}


