package com.example.stream;

public class Student {

    private final String name;
    private final  int age;


    private Student(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public static Student of(String name,int age) {
        return new Student(name, age);
    }


    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }


    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
