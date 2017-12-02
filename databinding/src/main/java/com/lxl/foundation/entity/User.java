package com.lxl.foundation.entity;

/**
 * Created by Administrator on 2015/12/6.
 */
public class User {
    private final String firstName;
    private final String lastName;
    private final int age;
    public User(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age=age;
    }

    public int getAge() {
        return age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
