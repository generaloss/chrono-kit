package generaloss.chronokit;

public class Sync {

    private long lastTimeNanos;
    private long targetDeltaNanos;
    private boolean enabled;

    public Sync(double rate) {
        this.setRate(rate);
        this.enable(true);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable(boolean enabled) {
        this.enabled = enabled;
        if(enabled)
            lastTimeNanos = System.nanoTime();
    }


    public double getRate() {
        if(targetDeltaNanos == 0L)
            return 0D;
        return (TimeUtils.NANOS_IN_SEC_D / targetDeltaNanos);
    }

    public void setRate(double rate) {
        if(rate <= 0D) {
            targetDeltaNanos = 0L;
        }else{
            targetDeltaNanos = (long) (TimeUtils.NANOS_IN_SEC_D / rate);
        }
        lastTimeNanos = System.nanoTime();
    }


    public void sync() {
        if(!enabled || targetDeltaNanos == 0L) {
            lastTimeNanos = System.nanoTime();
            return;
        }

        final long now = System.nanoTime();
        final long delta = (now - lastTimeNanos);
        final long sleepNanos = (targetDeltaNanos - delta);
        if(sleepNanos >= 0L)
            TimeUtils.sleepNanos(sleepNanos);

        lastTimeNanos = System.nanoTime();
    }

}