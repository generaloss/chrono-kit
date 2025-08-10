package generaloss.chronokit;


public class DeltaTimeCounter {

    private long lastTime;
    private float deltaTime;

    public DeltaTimeCounter() {
        this.lastTime = System.nanoTime();
    }

    public void update() {
        final long currentTime = System.nanoTime();
        deltaTime = (currentTime - lastTime) / TimeUtils.NANOS_IN_SEC_F;
        lastTime = currentTime;
    }

    public void reset() {
        lastTime = System.nanoTime();
        deltaTime = 0F;
    }

    public float get() {
        return deltaTime;
    }

}
