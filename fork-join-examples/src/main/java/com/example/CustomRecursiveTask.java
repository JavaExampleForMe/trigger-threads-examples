package com.example;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class CustomRecursiveTask extends RecursiveTask<Integer> {
    private int[] arr;

    private static final int THRESHOLD = 2;

    public CustomRecursiveTask(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD) {
            int sum = ForkJoinTask.invokeAll(createSubtasks(arr))
                    .stream()
                    .mapToInt(customRecursiveTask -> {
                        try {
                            return customRecursiveTask.get();    // a btter way to user customRecursiveTask.join since it doesn't throw exception
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    })
                    .sum();
            System.out.println(Thread.currentThread().getName() + Arrays.toString(arr) + " This sum - (" + sum + ") - was processed.");
            return sum;
        } else {
            return processing(arr);
        }
    }

    private Collection<CustomRecursiveTask> createSubtasks(int[] array) {
        int[] arr1 = Arrays.copyOfRange(array, 0, array.length / 2);
        int[] arr2 = Arrays.copyOfRange(array, array.length / 2, array.length);
        List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new CustomRecursiveTask(arr1));
        dividedTasks.add(new CustomRecursiveTask(arr2));
        return dividedTasks;
    }

    private Integer processing(int[] array) {
        int result = Arrays.stream(array)
                .sum();
        System.out.println(Thread.currentThread().getName() + Arrays.toString(array) + " result - (" + result + ") - was processed.");
        return result;
    }
}