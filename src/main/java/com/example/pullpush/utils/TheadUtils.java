package com.example.pullpush.utils;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TheadUtils {


    public static long getFutureLong(Future<Long> future) {
        return Optional.ofNullable(future).map(f -> {
            long result = 0;
            try {
                result = f.get();
            } catch (InterruptedException | ExecutionException e) {
                f.cancel(true);
                e.printStackTrace();
            }
            return result;
        }).orElse(0L);

    }
}
