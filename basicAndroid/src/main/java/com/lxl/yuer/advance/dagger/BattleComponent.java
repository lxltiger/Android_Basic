package com.lxl.yuer.advance.dagger;

import dagger.Component;

@Component(modules = BraavosModule.class)
public interface BattleComponent {
    War getWar();
    Starks stark();

    Boltons bolton();

    Cash getCash();
    Soldiers getSoldiers();
}