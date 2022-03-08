package com.hxm.rabbitma.utils;

/**
 * @author hxmao
 * @date 2022/2/28 17:12
 */
public class SleepUtils {

    public static void sleep(int m){

        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }
}
