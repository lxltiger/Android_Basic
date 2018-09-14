package com.lxl.tiger.designpattern.proxy.dynamic;

import java.lang.reflect.Proxy;
import java.util.HashMap;

public class Client {
    HashMap<String, PersonBean> datingDB = new HashMap<String, PersonBean>();


    public Client() {
        initializeDatabase();
    }

    void initializeDatabase() {
        PersonBean joe = new PersonBeanImpl();
        joe.setName("Joe Javabean");
        joe.setInterests("cars, computers, music");
        joe.setHotOrNotRating(7);
        datingDB.put(joe.getName(), joe);

        PersonBean kelly = new PersonBeanImpl();
        kelly.setName("Kelly Klosure");
        kelly.setInterests("ebay, movies, music");
        kelly.setHotOrNotRating(6);
        datingDB.put(kelly.getName(), kelly);
    }

    PersonBean getPersonFromDatabase(String name) {
        return (PersonBean)datingDB.get(name);
    }

    public static void main(String[] args) {
        Client client=new Client();

        PersonBean joe_javabean = client.getPersonFromDatabase("Joe Javabean");
        PersonBean ownerProxy = client.getOwnerProxy(joe_javabean);
        ownerProxy.setInterests("basketball");
        System.out.println(ownerProxy.getInterests());
        try {
            ownerProxy.setHotOrNotRating(12);
        } catch (Exception e) {
            System.out.println("can not set HotOrNotRating yourself");
        }

        PersonBean nonOwnerProxy = client.getNonOwnerProxy(joe_javabean);
        try {
            nonOwnerProxy.setInterests("football");
        } catch (Exception e) {
            System.out.println("can not set anything yourself");

        }

        nonOwnerProxy.setHotOrNotRating(8);

        System.out.println(nonOwnerProxy.getHotOrNotRating());

    }



    PersonBean getOwnerProxy(PersonBean person) {
        return  (PersonBean) Proxy.newProxyInstance(person.getClass().getClassLoader(),
                person.getClass().getInterfaces(),
                new OwnerInvocationHandler(person));
    }


    PersonBean getNonOwnerProxy(PersonBean person) {
        return  (PersonBean) Proxy.newProxyInstance(person.getClass().getClassLoader(),
                person.getClass().getInterfaces(),
                new NonOwnerInvocationHandler(person));
    }
}
