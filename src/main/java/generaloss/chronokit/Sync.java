package generaloss.chronokit;

import generaloss.spatialmath.Maths;

public class Sync {

    private long lastTime;
    private int targetDeltaTime;
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
    }


    public double getRate() {
        if(targetDeltaTime == 0)
            return 0D;
        return (Maths.NANOS_IN_SECf / targetDeltaTime);
    }

    public void setRate(double rate) {
        if(rate == 0D)
            return;

        targetDeltaTime = (int) (Maths.MILLIS_IN_SECf / rate); // time between frames with a given number of ticks per second
        lastTime = System.currentTimeMillis();                 // to calculate the time between frames
    }


    public void sync() {
        if(!enabled || targetDeltaTime == 0)
            return;

        final long deltaTime = (System.currentTimeMillis() - lastTime); // current time between frames
        if(deltaTime >= 0L){
            final long sleepTime = (targetDeltaTime - deltaTime); // time to adjust tick per second
            if(sleepTime > 0L)
                TimeUtils.delayMillis(sleepTime);
        }

        lastTime = System.currentTimeMillis();
    }

}