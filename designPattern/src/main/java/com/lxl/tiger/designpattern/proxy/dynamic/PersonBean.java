package com.lxl.tiger.designpattern.proxy.dynamic;

public interface PersonBean {
    String getName();

    String getGender();

    String getInterests();

    int getHotOrNotRating();

    void setName(String name);

    void setInterests(String interests);

    void setHotOrNotRating(int rating);
}