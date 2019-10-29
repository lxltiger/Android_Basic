package com.example.basic;

import java.util.*;
public class Type3 {
	
	public static void main(String[] args) {
		List<IFactory<? extends Part>> parts=new ArrayList<IFactory<? extends Part>>();
		parts.add(new Filter.Factory());
		parts.add(new FuleFilter.Factory());
		System.out.println(parts);
		Random random=new Random();
		for (int i=0; i<10;i++) {
			int next=random.nextInt(parts.size());
			System.out.println(parts.get(next).create());
		}
	}
}

 interface IFactory<T>{
	T create();
}

class Part{
	@Override
	public String toString(){
		return getClass().getSimpleName();
	}
}
class Filter extends Part{
	public static class Factory implements IFactory<Filter>{
		public Filter create(){

		return new Filter();
		}
	}
}
class FuleFilter extends Part{
	public static class Factory implements IFactory<FuleFilter>{
	public	FuleFilter create(){
			return  new FuleFilter();
		}
	}
}