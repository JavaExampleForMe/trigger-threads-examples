package completable.future;

import completable.future.components.HelloService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;


@EnableAutoConfiguration
@ComponentScan(basePackages = {"completable.future.components"})
@Slf4j
public class Main {

    public static void main(String[] args) {


        final ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        final HelloService helloService = context.getBean(HelloService.class);


        completableFuture3(helloService);
        //forkJoinTask(helloService);


        log.error("Something else is wrong here");
        log.error(">> Application Started");
    }

    @SneakyThrows
    private static void completableFuture(HelloService helloService) {
        // Default thread-pool of Java ~(2*CPU-1)
        final CompletableFuture<Void> executeDeletion = CompletableFuture.runAsync(() -> {
            helloService.sayHello();
        });

        executeDeletion.get(10, SECONDS);
    }

    @SneakyThrows
    private static void completableFuture2(HelloService helloService) {
        // A new Thread is created not taken from Java thread-pool
        final ExecutorService myThreadPool = Executors.newSingleThreadExecutor();
        final CompletableFuture<Void> executeDeletion = CompletableFuture.runAsync(() -> {

            helloService.sayHello();
        }, myThreadPool);

        executeDeletion.get(10, SECONDS);
    }

    @SneakyThrows
    private static void completableFuture3(HelloService helloService) {
        // A new Thread is created not taken from Java thread-pool
        final ExecutorService myThreadPool2 = Executors.newFixedThreadPool(4);
        System.out.println("Main ThreadName=" + Thread.currentThread().getName());
        final Future<ExecutorService> submit = myThreadPool2.submit(() -> {
            System.out.println("In Lambda ThreadName=" + Thread.currentThread().getName());

//            IntStream.range(0, 1000)
//                    .parallel()
//                    .unordered()
//                    .forEach(x -> System.out.println("x=" + x + " ThreadName=" + Thread.currentThread().getName()));
            helloService.sayHello();
        }, myThreadPool2);

        submit.get();

    }

    @SneakyThrows
    private static void completableFuture4(HelloService helloService) {
        // A new Thread is created not taken from Java thread-pool
        final ExecutorService myThreadPool2 = Executors.newFixedThreadPool(4);
        System.out.println("Main ThreadName=" + Thread.currentThread().getName());
        final CompletableFuture<Void> executeDeletion = CompletableFuture.supplyAsync(() -> {
            System.out.println("In Lambda ThreadName=" + Thread.currentThread().getName());

            IntStream.range(0, 1000)
                    .parallel()
                    .unordered()
                    .forEach(x -> System.out.println("x=" + x + " ThreadName=" + Thread.currentThread().getName()));
            helloService.sayHello();
            return 4;
        }, myThreadPool2)
                .thenAccept(numberFromLastOperation -> System.out.println(numberFromLastOperation));
    }

    @SneakyThrows
    private static void forkJoinTask(HelloService helloService) {
        // Default thread-pool of Java ~(2*CPU-1)
        // parallelStream is using by default Default thread-pool of Java
        ForkJoinTask forkJoinPool = ForkJoinPool.commonPool()
                .submit(() -> {
                    helloService.sayHello();
                });

        try {
            forkJoinPool.get(10, SECONDS);
        } catch (
                TimeoutException e) {
            log.debug("Got to timeout");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            log.debug("Got InterruptedException");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            log.debug("Got ExecutionException");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
