package threads.work.stealing;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newWorkStealingPool(1);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
        ForkJoinPool workStealingPool = (ForkJoinPool)executorService;
        RecursiveRunnable recursiveRunnable = new RecursiveRunnable(4);
        workStealingPool.invoke(recursiveRunnable);
//        fixedThreadPool.submit(recursiveRunnable);
//        fixedThreadPool.shutdown();

    }
}
