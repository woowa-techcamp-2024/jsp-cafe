package com.woowa.cafe.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ArticleCountCache {

    private static final AtomicInteger count = new AtomicInteger(0);

    public static void increase() {
        count.incrementAndGet();
    }

    public static void decrease() {
        count.decrementAndGet();
    }

    public static int getCount() {
        return count.get();
    }

    public static void setCount(int count) {
        ArticleCountCache.count.set(count);
    }
}
