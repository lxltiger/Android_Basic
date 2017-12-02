package com.example;

/**
 * 不正确的发布
 * public Holder holder;
 * public void initialize() {
 * holder = new Holder(42);
 * }
 * 如果Holder类使用以上方式发布，会导致其它线程观察的不一致性，也就是值会突然变化即使无人修改
 * 使用上述方法初始化发布后，如果在非发布线程调用assertSanity会抛出异常
 * 一个对象的引用对一个线程可见并不意味着对象的状态也对其同样可见？？
 * 使用以下方式是安全的
 * public static Holder holder = new Holder(42);
 * 原文解释
 * Static initializers are executed by the JVM at class initialization time; because
 * of internal synchronization in the JVM, this mechanism is guaranteed to safely
 * publish any objects initialized in this way
 * static initializers are executed by the jvm at class initialization time;
 * because of internal synchronization in the jvm,
 * this mechanism is guaranteed to safely publish any objects initialized in this way
 * 翻译
 * 静态初始化器在类初始化的时候由JVM执行的；
 * 因为在JVM内部同步，所以这种方式初始化保证安全地发布任何物体
 */
public class Holder {
    private int n;

    public Holder(int n) {
        this.n = n;
    }

    public void assertSanity() {
        if (n != n)
            throw new AssertionError("This statement is false.");
    }
}