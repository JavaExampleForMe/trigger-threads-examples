package com.example;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class CustomCallable implements Callable<Integer> {
    private static final int THRESHOLD = 2;
    private int[] array;
    private ExecutorService executorService;

    public CustomCallable(int[] array, ExecutorService executorService) {
        this.array = array;
        this.executorService = executorService;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        Thread.currentThread().getThreadGroup().list();

        log.debug("Thread started, threadId={}  threadName={} activeCount={} array=[{}] ", Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.activeCount(), Arrays.toString(array) );
        if (array.length > THRESHOLD) {
            List<Callable<Integer>> dividedTasks = createSubtasks(array, executorService);
            sum = executorService.invokeAll(dividedTasks).stream()
                    .mapToInt(feature -> {
                        try {
                            return feature.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    })
                    .sum();
       } else {
            sum = processing(array);
        }
        log.debug("Thread ended, threadId={}  sum[{}]={} ", Thread.currentThread().getId(), Arrays.toString(array) ,sum);
        return sum;
    }

    private List<Callable<Integer>> createSubtasks(int[] array, ExecutorService executorService) {
        int[] arr1 = Arrays.copyOfRange(array, 0, array.length / 2);
        int[] arr2 = Arrays.copyOfRange(array, array.length / 2, array.length);
        List<Callable<Integer>> dividedTasks = new ArrayList<>();
        dividedTasks.add(new CustomCallable(arr1, executorService));
        dividedTasks.add(new CustomCallable(arr2, executorService));
        return dividedTasks;
    }

    private Integer processing(int[] array) {
        int result = Arrays.stream(array)
                .sum();
        return result;
    }
}
