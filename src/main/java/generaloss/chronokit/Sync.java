package generaloss.chronokit;

public class Sync {

    private boolean enabled;
    private long lastTimeNanos;
    private long deltaNanos, targetDeltaNanos;
    private long sleepNanos;

    public Sync(double rate) {
        this.setEnabled(true);
        this.setRate(rate);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(!enabled)
            this.updateLastTime();
    }

    public void enable() {
        this.setEnabled(true);
    }

    public void disable() {
        this.setEnabled(false);
    }


    private void updateLastTime() {
        lastTimeNanos = System.nanoTime();
    }


    public long getWorkDeltaNanos() {
        return deltaNanos;
    }

    public long getTargetDeltaNanos() {
        return targetDeltaNanos;
    }

    public long getSleepNanos() {
        return sleepNanos;
    }


    public float getRate() {
        if(targetDeltaNanos == 0L)
            return 0F;
        return (TimeUtils.NANOS_IN_SEC_F / targetDeltaNanos);
    }

    public void setRate(double rate) {
        if(rate <= 0D) {
            targetDeltaNanos = 0L;
        }else{
            targetDeltaNanos = (long) (TimeUtils.NANOS_IN_SEC_D / rate);
        }
        this.updateLastTime();
    }


    public void sync() {
        deltaNanos = (System.nanoTime() - lastTimeNanos);

        if(!enabled || targetDeltaNanos == 0L) {
            this.updateLastTime();
            return;
        }

        sleepNanos = (targetDeltaNanos - deltaNanos);
        if(sleepNanos >= 0L)
            TimeUtils.delayNanos(sleepNanos);

        this.updateLastTime();
    }

}