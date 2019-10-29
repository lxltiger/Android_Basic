package com.example.basic;

class Demo1{
	public static void main(String[] args){
		Demo1 demo=new Demo1();
		// demo.cast();
		demo.binary();

	}


	//类型转换

	private void cast(){
		byte b=2;
		// b=b+3;此句会把b提升为int类型，然后相加赋给byte类型的b，从而导致精度损失，编译不通过
		//强转,注意mac中英文下等号不一样，无法编译
		b=(byte)(b+3);
		System.out.println("b="+b);
		//内部实行转换
		b+=3;
		System.out.println("b="+b);

		//转义字符
		System.out.println("\"hello world\"");

	}
	//转二进制，使用与操作和位移
	private void binary(){
		int x=-16;
		// 存储二进制的数组
		int[] bin=new int[32];
		for (int i=0;i<32;i++) {
			//取最后一位并倒叙存放到数组中
			bin[31-i]=x&1;
			//把数据右移一位,无符号右移；防止负数右移不尽
			x=x>>>1;
		}
		for (int i=0;i<bin.length ;i++ ) {
			System.out.print(bin[i]);
			
		}




	}
}