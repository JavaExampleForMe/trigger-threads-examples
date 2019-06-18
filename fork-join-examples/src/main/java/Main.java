import com.example.CustomCallable;
import com.example.CustomRecursiveTask;

import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws Exception {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        int[] myArray = {10, 20, 30, 40, 50, 40, 60, 55, 77, 88, 22, 53, 4, 8,4, 6, 1,2,4,6,7,};
//        CustomRecursiveTask customRecursiveTask = new CustomRecursiveTask(myArray);
//        // One way to execute
//        forkJoinPool.execute(customRecursiveTask);
//        int result1 = customRecursiveTask.join();
//
//
//        // Two way to execute
//        int result2 = forkJoinPool.invoke(customRecursiveTask);

        CustomCallable customCallable = new CustomCallable(myArray);
        customCallable.call();

    }
}
