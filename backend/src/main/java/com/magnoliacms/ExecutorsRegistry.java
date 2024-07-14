package com.magnoliacms;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ExecutorsRegistry {

    @Produces
    @ApplicationScoped
    @Named("dbExecutor")
    public Executor dbExecutor() {
        return new ThreadPoolExecutor(1, 20, 60L, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>());
    }
}
