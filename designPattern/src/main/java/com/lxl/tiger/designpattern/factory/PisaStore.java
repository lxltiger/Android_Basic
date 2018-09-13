package com.lxl.tiger.designpattern.factory;

public abstract class PisaStore {

    public Pisa orderPisa(String type) {
        Pisa pisa = cretePias(type);
        pisa.prepare();
        pisa.bake();
        pisa.cut();
        pisa.box();
        return pisa;
    }

    protected abstract Pisa cretePias(String type);
}
