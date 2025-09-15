package generaloss.chronokit;

public class Test {

    public static void main(String[] args) {
        AsyncRunnable runnable = new AsyncRunnable(
            () -> System.out.println("tick")
        );

        System.out.println("start");
        runnable.runInterval(1000L, 500L);

        TimeUtils.waitFor(runnable::isTerminated);
    }

}
