package com.jongsoft.lang.perfmon;

import java.util.function.Supplier;

class Timer {

    private final String name;
    private final String process;
    private int executions;
    private long averageTime;

    Timer(String name, String process) {
        this.name = name;
        this.process = process;
    }

    public <T> T execute(Supplier<T> execution) {
        long startTime = System.nanoTime();
        try {
            return execution.get();
        } finally {
            long executionTime = System.nanoTime() - startTime;
        }
    }

    public void execute(Runnable execution) {
        long startTime = System.nanoTime();
        try {
            execution.run();
        } finally {
            long executionTime = System.nanoTime() - startTime;
        }
    }

    private synchronized void executed(long duration) {
        executions++;
        averageTime += duration;
    }

}
