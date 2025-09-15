package generaloss.chronokit;

public class Stopwatch {

    private long lastTimeNanos, timeNanos;
    private boolean active, paused;

    public Stopwatch start() {
        if(active)
            return this;
        active = true;
        return this.reset();
    }

    public Stopwatch reset() {
        timeNanos = System.nanoTime();
        lastTimeNanos = timeNanos;
        return this;
    }

    public Stopwatch stop() {
        active = false;
        return this;
    }

    public Stopwatch pause() {
        paused = true;
        return this;
    }

    public Stopwatch resume() {
        paused = false;
        return this;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isActive() {
        return active;
    }

    public Stopwatch setNanos(long nanos) {
        lastTimeNanos = (timeNanos - nanos);
        return this;
    }

    public Stopwatch setMillis(double millis) {
        return this.setNanos(Math.round(millis * TimeUtils.NANOS_IN_MS_D));
    }

    public Stopwatch setSeconds(double seconds) {
        return this.setMillis(seconds * TimeUtils.MILLIS_IN_SEC_D);
    }

    public Stopwatch setMinutes(double minutes) {
        return this.setSeconds(minutes * 60D);
    }

    public Stopwatch setHours(double hours) {
        return this.setMinutes(hours * 60D);
    }

    public Stopwatch setDays(double days) {
        return this.setHours(days * 24D);
    }

    public long getNanos() {
        if(!active)
            return 0L;
        if(!paused)
            timeNanos = System.nanoTime();
        return (timeNanos - lastTimeNanos);
    }

    public float getMillis() {
        return (this.getNanos() / TimeUtils.NANOS_IN_MS_F);
    }

    public float getSeconds() {
        return (this.getMillis() / TimeUtils.MILLIS_IN_SEC_F);
    }

    public float getMinutes() {
        return (this.getSeconds() / 60F);
    }

    public float getHours() {
        return (this.getMinutes() / 60F);
    }

    public float getDays() {
        return (this.getHours() / 24F);
    }

}
