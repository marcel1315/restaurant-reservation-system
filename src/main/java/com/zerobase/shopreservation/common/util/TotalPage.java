package com.zerobase.shopreservation.common.util;

public class TotalPage {

    // 0 row -> 1 page
    // 1 row -> 1 page
    // 10 row -> 1 page
    // 11 row -> 2 page
    // 20 row -> 2 page
    public static long of(long totalCount, long pageSize) {
        return Math.max(totalCount - 1, 0) / pageSize + 1;
    }
}
