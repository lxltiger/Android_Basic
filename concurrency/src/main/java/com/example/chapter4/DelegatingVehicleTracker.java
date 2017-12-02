package com.example.chapter4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by 李晓林 on 2016/12/21
 * qq:1220289215
 * 对MonitorVehicleTracker线程安全的修改
 * 使用线程安全的类来代理
 */

public class DelegatingVehicleTracker {
    private final ConcurrentMap<String,Point> mPoints;
    private final Map<String,Point> mUnmodifiedMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        mPoints = new ConcurrentHashMap<>(points);
        mUnmodifiedMap = Collections.unmodifiableMap(mPoints);
    }

    /**
     * 对ConcurrentMap的修改会动态显示
     * @return 一个动态的Map
     */
    public Map<String, Point> getLocations() {
        return mUnmodifiedMap;
    }

    /**
     *
     * @return 一个静态的复制
     */
    public Map<String, Point> getLocations2() {
        return Collections.unmodifiableMap(new HashMap<>(mPoints));
    }

    public Point getLocation(String id) {
        return mUnmodifiedMap.get(id);
    }

    /**
     * ConcurrentMap 支持并发 Point是不可变的
     * 确保了 线程是安全的
     * @param id
     * @param x
     * @param y
     */
    public void setPoints(String id, int x, int y) {
        if (mPoints.replace(id, new Point(x, y)) == null) {
            throw new IllegalArgumentException("no such id " + id);
        }
    }
}
