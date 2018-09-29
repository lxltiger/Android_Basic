package com.lxl.tiger.designpattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Typesafe heterogeneous container pattern
public class Favorite {
    private Map<Class<?>, Object> map = new HashMap<>();

//    cls.cast(instance)动态转化防止使用raw type： Class a=Integer.class;
    public <T> void put(Class<T> cls, T instance) {
        map.put(cls, cls.cast(instance));
    }

    public <T> T get(Class<T> cls) {
        return cls.cast(map.get(cls));
    }

    public static void main(String[] args) {
        Favorite favorite=new Favorite();
        Class<Integer> a=Integer.class;
        favorite.put(a, 2);
        Integer o = favorite.get(a);

        List<String> temp = Collections.checkedList(new ArrayList<>(),String.class);
        List s=new ArrayList();
        s.add(1);
        temp.addAll(s);

    }
}
