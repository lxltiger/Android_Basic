package com.lxl.tiger.designpattern.builder;

public class NyPizza extends Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }
    private final Size size;

    static class Builder extends Pizza.Builder<Builder>{
        private final Size size;

        public Builder(Size size) {
            this.size = size;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public NyPizza build() {
            return new NyPizza(this);
        }
    }


    public NyPizza(Builder builder) {
        super(builder);
        size=builder.size;
    }
}
