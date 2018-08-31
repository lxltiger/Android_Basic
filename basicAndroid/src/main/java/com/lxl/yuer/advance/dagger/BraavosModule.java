package com.lxl.yuer.advance.dagger;

import dagger.Module;
import dagger.Provides;

@Module //The module
public class BraavosModule {
    /*Cash cash;
    Soldiers soldiers;

    public BraavosModule(Cash cash, Soldiers soldiers){
        this.cash=cash;
        this.soldiers=soldiers;
    }*/

    @Provides
        //Provides cash dependency
    Cash provideCash(){
        return new Cash();
    }

    @Provides //provides soldiers dependency
    Soldiers provideSoldiers(){
        return new Soldiers();
    }

}