package com.example.pullpush.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TheadUtils {


    public static long getFutureLong(Future<Long> future){
        long result = 0;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            future.cancel(true);
            e.printStackTrace();
        }
        return result;
    }
}
