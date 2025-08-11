package generaloss.chronokit;


public class DeltaTimeCounter {

    private long lastTimeNanos;
    private float deltaTimeSeconds;

    public DeltaTimeCounter() {
        this.lastTimeNanos = System.nanoTime();
    }

    public void update() {
        final long nowNanos = System.nanoTime();
        deltaTimeSeconds = (nowNanos - lastTimeNanos) / TimeUtils.NANOS_IN_SEC_F;
        lastTimeNanos = nowNanos;
    }

    public void reset() {
        lastTimeNanos = System.nanoTime();
        deltaTimeSeconds = 0F;
    }

    public float get() {
        return deltaTimeSeconds;
    }

}
