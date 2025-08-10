package generaloss.chronokit;

public class PerSecondCounter {

    private long lastTime;
    private int counter, rate;

    public PerSecondCounter() {
        this.lastTime = System.nanoTime();
    }

    public void update() {
        final long currentTime = System.nanoTime();

        final long difference = (currentTime - lastTime);
        if(difference > TimeUtils.NANOS_IN_SEC_L){
            lastTime = currentTime;

            rate = counter;
            counter = 0;
        }else
            counter++;
    }

    public void reset() {
        lastTime = System.nanoTime();
        counter = 0;
        rate = 0;
    }

    public int get() {
        return rate;
    }

}
