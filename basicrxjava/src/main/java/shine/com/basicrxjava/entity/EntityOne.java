package shine.com.basicrxjava.entity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by Administrator on 2016/8/9.
 */
public class EntityOne {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public Observable<String> valueObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(name);
            }
        });
    }

}
