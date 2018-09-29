package com.lxl.tiger.designpattern.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RunTest {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> sampleClass = Sample2.class;
        Method[] declaredMethods = sampleClass.getDeclaredMethods();
        int test=0;
        int passed=0;
//        Sample o = (Sample) sampleClass.newInstance();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(ExceptionTest.class)) {
                test++;
                try {
                    method.invoke(null);
                    System.out.printf("Test %s failed no exception%n",method);

                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    Class<? extends Exception>[] value = method.getAnnotation(ExceptionTest.class).value();
                    int oldPassed=passed;
                    for (Class<? extends Exception> aClass : value) {
                        if (aClass.isInstance(cause)) {
                            passed++;
                            break;
                        }
                    }
                    if(oldPassed==passed){
                        System.out.printf("Test %s failed %s%n",method,cause);
                    }
                } catch (Exception e) {
                    System.out.println("invalid test%n "+method);
                }
            }
        }

        System.out.printf("%d test with %d passd%n",test,passed);
    }
}
