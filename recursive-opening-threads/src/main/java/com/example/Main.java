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

//        log.info("Start executing using ExecutorService");
//        long startTime = System.currentTimeMillis();
 //       ExecutorService executorService = Executors.newFixedThreadPool(5);
 //       ExecutorService executorService = Executors.newCachedThreadPool();//7765
        ExecutorService executorService = Executors.newWorkStealingPool();//57811
        CustomCallable customCallable = new CustomCallable(myArray, executorService);
        customCallable.call();


//        long endTime = System.currentTimeMillis();
//        long duration  = endTime - startTime;
        executorService.shutdownNow();
//        log.info("Finished executing using ExecutorService {}", duration);

//
        log.info("Start executing using ForkJoinPool");
        long startTime2 = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        System.out.println("ActiveThreadCount :" +forkJoinPool.getActiveThreadCount());
        System.out.println("RunningThreadCount :" + forkJoinPool.getRunningThreadCount());
        System.out.println("PoolSize :"+forkJoinPool.getPoolSize());
        System.out.println("Parallelism :"+ forkJoinPool.getParallelism());
        CustomRecursiveTask customRecursiveTask = new CustomRecursiveTask(myArray);
//        // One way to execute
        forkJoinPool.execute(customRecursiveTask);
        customRecursiveTask.join(); //566
        long endTime2 = System.currentTimeMillis();
        long duration2  = endTime2 - startTime2;
        log.info("Finished executing using ForkJoinPool {}", duration2);//


        // Two way to execute
//       forkJoinPool.invoke(customRecursiveTask);



    }
}
