package com.lxl.tiger.designpattern.builder;

public class Calzone extends Pizza {
    private final boolean sauceInside;
    static class Builder extends Pizza.Builder<Builder>{
        private  boolean sauceInside=false;

        public Builder sauceInsize() {
            sauceInside=true;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Calzone build() {
            return new Calzone(this);
        }
    }
    public Calzone(Builder builder) {
        super(builder);
        this.sauceInside=builder.sauceInside;
    }
}
