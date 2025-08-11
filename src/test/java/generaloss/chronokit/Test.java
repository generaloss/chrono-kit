package generaloss.chronokit;

public class Test {

    public static void main(String[] args) {
        final TickGenerator tg = new TickGenerator(2);
        final DeltaTimeCounter dtCounter = new DeltaTimeCounter();
        tg.start(() -> {
            System.out.println("tick " + tg.sync().getSleepNanos());
            dtCounter.update();
        });
    }

}
