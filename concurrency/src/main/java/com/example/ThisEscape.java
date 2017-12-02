package com.example;

public class ThisEscape {
    /**
     * 不要在构造函数中使用内部类，内部类对这个实例有一个隐式的引用，从而导致this逃逸
     * 这样在构造函数还没有完成就使this逃逸的为不正确构造，
     * 所以尽量不要在构造函数中开启线程
     * 同理调用复写的方法也会导致同样效果
     *
     * @param source
     */
    public ThisEscape(EventSource source) {
        source.registerListener(
                new EventListener() {
                    public void onEvent(Event e) {
                        doSomething(e);
                    }
                });
    }

    private void doSomething(Event e) {

    }

    /**
     * 如果一定要在构造函数中注册监听或开启线程
     * 使用工厂方法
     */
    static class SafeListener {
        private final EventListener mEventListener;

        private SafeListener() {
            mEventListener = new EventListener() {
                @Override
                public void onEvent(Event event) {
//                    dosomething
                }
            };
        }

        public static void getInstance(EventSource eventSource) {
            SafeListener safeListener=new SafeListener();
            eventSource.registerListener(safeListener.mEventListener);
        }
    }


    private class EventSource {
        void registerListener(EventListener listener) {

        }
    }

    interface EventListener {
        void onEvent(Event event);
    }

    private class Event {
    }
}