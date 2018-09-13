package com.lxl.tiger.designpattern.oberver;

import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Subject {

    private List<Observer> observerList = new ArrayList<>();

    private Data data;
    @Override
    public void register(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observerList.remove(observer);
    }

    public void displayData(Data data) {
        data = data;
        measurementsChanged();
    }

    public void measurementsChanged() {
        notifyAllObservers();
    }

    @Override
    public void notifyAllObservers() {
        for (Observer observer : observerList) {
            observer.update(data);
        }
    }
}
