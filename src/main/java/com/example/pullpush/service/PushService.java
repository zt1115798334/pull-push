package com.example.pullpush.service;

import java.util.concurrent.ExecutionException;

public interface PushService {

    void start() throws ExecutionException, InterruptedException;
}
