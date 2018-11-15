package com.example.argir.tree;

import java.util.Random;

public class Client {

    public static void main(String[] args) {

        LLRBST<Integer,Integer> bst=new LLRBST<>();

        Random random = new Random(100L);

        for (int i = 10; i > 0; i--) {
            int key=random.nextInt(10000);
            int value = random.nextInt();
            System.out.println(key );
            bst.put(key,value);
        }

        bst.listElement();

//        bst.deleteMin();

    }
}
