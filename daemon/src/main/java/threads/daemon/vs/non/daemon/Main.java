package threads.daemon.vs.non.daemon;

public class Main {
    public static void main(String[] args) {
        Runnable this_is_a_deamon = new Runnable() {
            public void run() {
                while (true) {
                    System.out.println("this is a deamon");
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Runnable this_is_not_a_deamon = new Runnable() {
            public void run() {
                while (true) {
                    System.out.println("this is not a deamon");
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread thread = new Thread(this_is_a_deamon);
        thread.setDaemon(true);
        thread.start();


        Thread thread1 = new Thread(this_is_not_a_deamon);
        thread1.setDaemon(false);
        thread1.start();
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main is terminated");

    }
}
