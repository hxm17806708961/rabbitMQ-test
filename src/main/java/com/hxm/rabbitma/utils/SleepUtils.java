package com.hxm.rabbitma.utils;

/**
 * @author hxmao
 * @date 2022/2/28 17:12
 */
public class SleepUtils {

    public static void sleep(int m){

        try {
            Thread.sleep(1000*m);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
