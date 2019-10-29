package com.example.basic;

public class BasicConcept{
	public static void main(String[] args) {
		BasicConcept basic=new BasicConcept();
		// basic.showSystemProperty();
		// basic.showOverLoad();
		basic.printArray(new Integer[]{1,2,3});
		basic.printArray(new Integer[]{1,2,3});
		basic.printArray(new Object[]{"one",2,3f,4.9});
		//使用可变参数
		basic.printArray2(1,3,4);
		basic.printArray2("one",34,basic);
		basic.printArray2((Object[])new Integer[]{1,3,4});

		basic.printArray(3,"name","hei");
		basic.printArray(0);
		basic.printArray(4,new String[]{"one","three"});


	}
	/**显示系统属性*/
	private void showSystemProperty(){
		// 在终端输出系统相关属性列表
		System.getProperties().list(System.out);
		// 用户名
		System.out.println(System.getProperty("user.name"));
		//库的路径
		System.out.println(System.getProperty("java.library.path"));

	}
	/**重载演示*/
	private void showOverLoad(){
		PrimitiveOverLoad overLoad=new PrimitiveOverLoad();
		//常量3会被当作int类型处理
		overLoad.fl(3);
		overLoad.f2(3);
		byte x=3;
		//当不能准确匹配，比如此处f2没有byte类型方法重载,
		//如果传入的参数小于方法中声明的形参，会被提升，如果大于，必须强制转换，否则编译不通过
		overLoad.f2(x);
		char ch=3;
		//char  类型列外，会直接提升到int，
		overLoad.f2(ch);

	}

	private void printArray(Object[] objs){
		for (Object obj:objs ) {
			System.out.print("  obj"+obj);
		}
		System.out.println();
	}
	private void printArray2(Object... objs){
		for (Object obj:objs ) {
			System.out.print("  obj"+obj);
		}
		System.out.println();
	}


	private void printArray(int required,String... words){
		System.out.println("required "+required);
		for (String s :words ) {
			System.out.print("words "+s);
		}
		System.out.println();
	}

}

/**
*原始数据类型的重载
*构造函数的权限与类同步，此处就是default
*/

class PrimitiveOverLoad{
	void fl(char ch){
		System.out.println("fl char");
	}
	void fl(int ch){
		System.out.println("fl int");
	}
	void fl(byte ch){
		System.out.println("fl byte");
	}
	void f2(float ch){
		System.out.println("f2 float");
	}
	void f2(int ch){
		System.out.println("f2int");
	}
	void f2(short ch){
		System.out.println("f2 short");
	}
	
}