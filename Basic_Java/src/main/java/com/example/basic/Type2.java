package com.example.basic;

import java.util.*;
public class Type2{
	public static void main(String[] args) throws Exception{
		// showPet();
		// showPet2();
		showPet3();

	}
	@SuppressWarnings("unchecked")
	private static void showPet() throws Exception{
		List<Class<? extends Pet>> pets=new ArrayList<Class<? extends Pet>>();
		String[] list=new String[]{
			"Cat","Dog","Bird","Rat"
		};
		for(String s:list){
			//类型转换为unchecked
			Class<? extends Pet> petClass=(Class<? extends Pet>)Class.forName(s);
			pets.add(petClass);

		}
		System.out.println(pets);
		//随机实例化集合中的对象
		Random random=new Random();
		ArrayList<Pet> petList=new ArrayList<Pet>();
		// 随机填充
		for (int i=0; i<10; i++) {
			int rand=random.nextInt(list.length);
			Pet pet=pets.get(rand).newInstance();
			petList.add(pet);		
		}
		System.out.println(petList);
		// 将实例对象分类
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		for(Pet pet:petList){
			if(pet instanceof Cat){
				count(map,"Cat");
			}
			if(pet instanceof Dog){
				count(map,"Dog");
			}
			if(pet instanceof Bird){
				count(map,"Bird");
			}
			if(pet instanceof Rat){
				count(map,"Rat");
			}
		}
		System.out.println(map);
	}

	private static void count(HashMap<String,Integer> map,String name){
		Integer number=map.get(name);
		if(null==number){
			map.put(name,1);
		}else{
			map.put(name,number+1);

		}
	}

	private static void showPet2() throws Exception{
		// ／／使用类字面常量,不需要捕获异常，编译时检查
		List<Class<? extends Pet>> pets=Arrays.asList(Cat.class,
			Dog.class,Bird.class,Rat.class,Mutt.class);
		// 统计随机生成的对象，有多层继承关系会重复计入
		LinkedHashMap<Class<? extends Pet>,Integer> mapPet=new LinkedHashMap<Class<? extends Pet>,Integer>();
		//初始化Map，使键为class对象，值为0
		for(Class<? extends Pet> pet:pets){
			mapPet.put(pet,0);
		}

		Random random=new Random();
		List<Pet> petList=new ArrayList<Pet>();
		// ／／随机填充
		for (int i=0; i<20; i++) {
			Pet pet=pets.get(random.nextInt(pets.size())).newInstance();

			petList.add(pet);
		}
		System.out.println(petList);
		//统计
		for(Pet pet:petList){
			count2(mapPet,pet);

		}
		System.out.println(mapPet);
	}

	private static void count2(LinkedHashMap<Class<? extends Pet>,Integer> mapPet,Pet pet){
		for(Map.Entry<Class<? extends Pet>,Integer> entry:mapPet.entrySet()){
			if(entry.getKey().isInstance(pet)){
				mapPet.put(entry.getKey(),entry.getValue()+1);
			}
		}

	}


	private static void showPet3() throws Exception{
		List<Class<?>> objs=Arrays.asList(Cat.class,
			Dog.class,Bird.class,Rat.class,Mutt.class,Type2.class);
		Random random=new Random();
		List<Object> petList=new ArrayList<Object>();
		for (int i=0; i<20; i++) {
			Object obj=objs.get(random.nextInt(objs.size())).newInstance();
			petList.add(obj);


		}
		System.out.println(petList);
		HashMap<Class<?>,Integer> map=new HashMap<Class<?>,Integer>();
		for(Object pet:petList){
			if(Pet.class.isAssignableFrom(pet.getClass())){
				count3(map,pet.getClass());
			}else{
				System.out.println(pet.getClass().getSimpleName()+" not Pet ");
			}
		}
		System.out.println(map);

	}

	private static void count3(HashMap<Class<?>,Integer> map,Class<?> obj){
		Integer number=map.get(obj);
		if(null==number){
			map.put(obj,1);
		}else{
			map.put(obj,number+1);
		}
		Class<?> superClass=obj.getSuperclass();
		if(superClass!=null && Pet.class.isAssignableFrom(superClass)){
			count3(map,superClass);
		}

	}



}


class Pet{}
class Cat extends Pet{}
class Dog extends Pet{}
class Bird extends Pet{}
class Rat extends Pet{}
class Mutt extends Dog{}

