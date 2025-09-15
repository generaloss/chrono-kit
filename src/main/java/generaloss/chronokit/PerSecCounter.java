package generaloss.chronokit;

public class PerSecCounter {

    private long lastTime;
    private int counter, count;

    public PerSecCounter() {
        this.lastTime = System.nanoTime();
    }

    public void update() {
        final long currentTime = System.nanoTime();

        final long difference = (currentTime - lastTime);
        if(difference > TimeUtils.NANOS_IN_SEC_L){
            lastTime = currentTime;

            count = counter;
            counter = 0;
        }else
            counter++;
    }

    public void reset() {
        lastTime = System.nanoTime();
        counter = 0;
        count = 0;
    }

    public int getCount() {
        return count;
    }

}
