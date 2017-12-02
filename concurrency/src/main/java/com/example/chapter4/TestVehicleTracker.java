package com.example.chapter4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by 李晓林 on 2016/12/21
 * qq:1220289215
 */

public class TestVehicleTracker {
    private final ConcurrentMap<String,String> mConcurrentMap;
    private final Map<String,String> unModifiedMap;

    public TestVehicleTracker(Map<String,String> map) {
        mConcurrentMap = new ConcurrentHashMap<>(map);
        unModifiedMap = Collections.unmodifiableMap(mConcurrentMap);
    }

    private void set() {
        mConcurrentMap.put("one", "one1");
    }

    /**
     *
     * @return mConcurrentMap的最新内容
     */
    private Map<String, String> get() {
        return unModifiedMap;
    }

    /**
     *
     * @return mConcurrentMap的内容快照
     */
    private Map<String, String> get2() {
        return Collections.unmodifiableMap(new HashMap<String, String>(mConcurrentMap));
    }



    static class Test {
        public static void main(String[] args) {
            Map<String, String> map = new HashMap<>();
            map.put("one", "one");
            map.put("two", "two");
            map.put("three", "three");
            TestVehicleTracker delegatingVehicleTracker=new TestVehicleTracker(map);
            Test test=new Test();
            test.one(delegatingVehicleTracker);
            test.two(delegatingVehicleTracker);

        }

        /**
         * 打印当前内容后休眠2秒等待two修改内容后再打印
         * @param tracker
         * 输出结果
         * [one, two, three]
        *[one1, two, three]
         * 其他线程对ConcurrentMap的修改对unModifiedMap都是可见的
         */
        private void one(final TestVehicleTracker tracker) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        Map<String, String> map = tracker.get();
                        Map<String, String> map2 = tracker.get2();
                        System.out.println(map.values().toString());
                        System.out.println(map2.values().toString());
                        sleep(2000);
                        System.out.println(map.values().toString());
                        System.out.println(map2.values().toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        //休眠1秒让one先打印当前数组再修改
        private void two(final TestVehicleTracker tracker) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(1000);
                        tracker.set();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


}
