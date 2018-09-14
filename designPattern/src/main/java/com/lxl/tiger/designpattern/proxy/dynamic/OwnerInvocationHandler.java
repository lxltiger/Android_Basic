package com.lxl.tiger.designpattern.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OwnerInvocationHandler implements InvocationHandler {
    PersonBean personBean;

    public OwnerInvocationHandler(PersonBean personBean) {
        this.personBean = personBean;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws IllegalAccessException {
        try {
            if (method.getName().startsWith("get")) {
                return method.invoke(personBean, objects);
            } else if (method.getName().equals("setHotOrNotRating")) {
                throw new IllegalAccessException();
            } else if (method.getName().startsWith("set")) {
              return   method.invoke(personBean, objects);
            }
        }  catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
