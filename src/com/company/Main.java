package com.company;

import java.util.concurrent.Semaphore;

public class Main {
    static final Semaphore starter = new Semaphore(0);
    static final Object startMutex = new Object();
    static final Semaphore finisher = new Semaphore(0);

    static final int TOTAL_TASKS = 100000000;
    static final int N_THREADS = 60;
    static final int THREAD_TASK = TOTAL_TASKS / N_THREADS;

    public static void main(String[] args) throws InterruptedException
    {
        long start;


        System.out.println("Creating threads...");
        for (int i = 0; i < N_THREADS; i++)
        {
            Thread thread = new Thread(() -> {
                run();
            });
            thread.start();

        }

        starter.acquire(N_THREADS);
        System.out.println("Created threads. Starting task...");

        start = System.currentTimeMillis();
        synchronized (startMutex) {
            startMutex.notifyAll();
        }
        System.out.println("Waiting for threads to finish...");
        finisher.acquire(N_THREADS);
        System.out.println("Threads finished. Stopping application...");

        long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

    static void run() {
        synchronized (startMutex) {
            starter.release();
            try
            {
                startMutex.wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        int result = 0;
        double r = Math.random();
        for (int i = 0; i < THREAD_TASK; i++) {
               Math.sin(result++ * r);
        }
        finisher.release();
    }
}
