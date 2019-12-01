package com.example;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        int[] myArray = IntStream.rangeClosed(1, 10).toArray();

        computeUsingExecutorService(myArray);

        computeUsingForkJoinPool(myArray);


    }

    private static void computeUsingForkJoinPool(int[] myArray) {
        log.info("Start executing using ForkJoinPool");
        long startTime2 = System.currentTimeMillis();
        // parallelism level of the ForkJoinPool is calculated by number of CPU cores - 1
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        // If we want to control the parallelism level of the ForkJoinPool
//        ExecutorService executorService = Executors.newWorkStealingPool(1);
//        ForkJoinPool forkJoinPool = (ForkJoinPool)executorService;

        System.out.println("ActiveThreadCount :" +forkJoinPool.getActiveThreadCount());
        System.out.println("RunningThreadCount :" + forkJoinPool.getRunningThreadCount());
        System.out.println("PoolSize :"+forkJoinPool.getPoolSize());
        System.out.println("Parallelism :"+ forkJoinPool.getParallelism());
        CustomRecursiveTask customRecursiveTask = new CustomRecursiveTask(myArray);
        // One way to execute
        forkJoinPool.execute(customRecursiveTask);
        // Second way to execute when we expect for result
       // forkJoinPool.invoke(customRecursiveTask);
        customRecursiveTask.join(); //566
        long endTime2 = System.currentTimeMillis();
        long duration2  = endTime2 - startTime2;
        log.info("Finished executing using ForkJoinPool {}", duration2);
    }

    private static void computeUsingExecutorService(int[] myArray) throws Exception {
        log.info("Start executing using ExecutorService");
        long startTime = System.currentTimeMillis();
        //       ExecutorService executorService = Executors.newFixedThreadPool(1);
        //       ExecutorService executorService = Executors.newCachedThreadPool();//7765
        ExecutorService executorService = Executors.newWorkStealingPool(1);//57811
        CustomCallable customCallable = new CustomCallable(myArray, executorService);
        customCallable.call();


        long endTime = System.currentTimeMillis();
        long duration  = endTime - startTime;
        executorService.shutdownNow();
        log.info("Finished executing using ExecutorService {}", duration);
    }
}
