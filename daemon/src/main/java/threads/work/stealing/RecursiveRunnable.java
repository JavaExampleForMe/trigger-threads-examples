package threads.work.stealing;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;

public class RecursiveRunnable extends RecursiveTask<Integer> implements Runnable {
    private final int n;

    public RecursiveRunnable(int n) {
        this.n = n;
    }

    @SneakyThrows
    protected Integer compute() {
        Thread thread = Thread.currentThread();
        System.out.println("Thread started, threadId=" + thread.getId() + " threadName=" + thread.getName() + " n=" + n +  " activeCount=" + Thread.activeCount());
        if (n <= 1) {
            return n;
        }
        RecursiveRunnable f1 = new RecursiveRunnable(n - 1);
        f1.fork();
        RecursiveRunnable f2 = new RecursiveRunnable(n - 2);
        f2.fork();
        int result = 0;
 //        result = f2.get() + f1.get();
         result = f2.get();
        System.out.println("Thread in the middle,  threadId=" + thread.getId() + " n=" + n +  " activeCount=" + Thread.activeCount() );

        result =result + f1.get();
        System.out.println("Thread finished,  threadId=" + thread.getId() + " n=" + n +  " activeCount=" + Thread.activeCount() + " result=" + result);
        return result;
    }

    @Override
    public void run() {
        compute();
    }
}
