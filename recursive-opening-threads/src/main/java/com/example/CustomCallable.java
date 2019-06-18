package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class CustomCallable implements Callable<Integer> {
    private static final int THRESHOLD = 2;
    private int[] array;

    public CustomCallable(int[] array) {
        this.array = array;
    }

    @Override
    public Integer call() throws Exception {
        if (array.length > THRESHOLD) {
            List<Callable<Integer>> dividedTasks = createSubtasks(array);
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            int sum = executorService.invokeAll(dividedTasks).stream()
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
            executorService.shutdownNow();
            System.out.println(Thread.currentThread().getName() + Arrays.toString(array) + " This sum - (" + sum + ") - was processed.");
            return sum;
        }
        else {
            return processing(array);
        }
    }

    private List<Callable<Integer>> createSubtasks(int[] array) {
        int[] arr1 = Arrays.copyOfRange(array, 0, array.length / 2);
        int[] arr2 = Arrays.copyOfRange(array, array.length / 2, array.length);
        List<Callable<Integer>> dividedTasks = new ArrayList<>();
        dividedTasks.add(new CustomCallable(arr1));
        dividedTasks.add(new CustomCallable(arr2));
        return dividedTasks;
    }

    private Integer processing(int[] array) {
        int result = Arrays.stream(array)
                .sum();
        System.out.println(Thread.currentThread().getName() + Arrays.toString(array) + " result - (" + result + ") - was processed.");
        return result;
    }
}
