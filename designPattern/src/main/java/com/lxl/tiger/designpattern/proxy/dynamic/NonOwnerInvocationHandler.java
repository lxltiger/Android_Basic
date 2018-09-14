package com.lxl.tiger.designpattern.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NonOwnerInvocationHandler implements InvocationHandler {
    PersonBean personBean;

    public NonOwnerInvocationHandler(PersonBean personBean) {
        this.personBean = personBean;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws IllegalAccessException {
        try {
            if (method.getName().equals("setHotOrNotRating")) {
               return method.invoke(personBean, objects);
            } else if (method.getName().startsWith("get")) {
               return method.invoke(personBean, objects);
            } else if (method.getName().startsWith("set")) {
                throw new IllegalAccessException();
            }

        } catch (InvocationTargetException e) {
            e.printStackTrace();

        }
        return null;
    }
}
