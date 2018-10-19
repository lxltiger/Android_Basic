package com.example.annotation;

/**
 * Created by  on 2017/3/24
 * qq:1220289215
 */

public class IdAlreadyExistException extends RuntimeException {
    private FactoryAnnotateClass mExisting;

    public IdAlreadyExistException() {
    }

    public IdAlreadyExistException(String message) {
        super(message);
    }


    public IdAlreadyExistException(FactoryAnnotateClass existing) {
        mExisting = existing;
    }

    public FactoryAnnotateClass getExisting() {
        return mExisting;
    }
}
