package generaloss.chronokit;

import java.util.function.BooleanSupplier;

public class TimeUtils {

    public static final float NANOS_IN_SEC_F = 1_000_000_000F;
    public static final float NANOS_IN_MS_F = 1_000_000F;
    public static final float MILLIS_IN_SEC_F = 1_000F;

    public static final double NANOS_IN_SEC_D = 1_000_000_000D;
    public static final double NANOS_IN_MS_D = 1_000_000D;
    public static final double MILLIS_IN_SEC_D = 1_000D;

    public static final int NANOS_IN_SEC_I = 1_000_000_000;
    public static final int NANOS_IN_MS_I = 1_000_000;
    public static final int MILLIS_IN_SEC_I = 1_000;

    public static final long NANOS_IN_SEC_L = 1_000_000_000L;
    public static final long NANOS_IN_MS_L = 1_000_000L;
    public static final long MILLIS_IN_SEC_L = 1_000L;

    private static final int SLEEP_THRESHOLD_MILLIS = 100;
    private static final long SLEEP_THRESHOLD_NANOS = (SLEEP_THRESHOLD_MILLIS * TimeUtils.NANOS_IN_MS_L);


    public static void sleepMillis(long millis) {
        try{
            Thread.sleep(millis);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public static void sleep(long millis, int nanos) {
        try{
            Thread.sleep(millis, nanos);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public static void sleepNanos(long nanos) {
        if(nanos <= 0L)
            return;

        final long millis = (nanos / NANOS_IN_MS_L);
        final int nanosPart = (int) (nanos % NANOS_IN_MS_L); // [0 .. 999_999]
        sleep(millis, nanosPart);
    }

    public static void sleepSeconds(double seconds) {
        if(seconds <= 0D)
            return;

        final long millis = (long) (seconds * MILLIS_IN_SEC_D);
        sleepMillis(millis);
    }


    public static void spinWaitNanos(long nanos) {
        final long current = System.nanoTime();
        while(System.nanoTime() - current < nanos)
            Thread.onSpinWait();
    }

    public static void spinWaitMillis(long millis) {
        final long current = System.currentTimeMillis();
        while(System.currentTimeMillis() - current < millis)
            Thread.onSpinWait();
    }

    public static void spinWaitSeconds(double seconds) {
        spinWaitNanos((long) (seconds * NANOS_IN_SEC_D));
    }


    public static void delayNanos(long nanos) {
        if(nanos > SLEEP_THRESHOLD_NANOS) {
            sleepNanos(nanos - SLEEP_THRESHOLD_NANOS); // reduce CPU load
            spinWaitNanos(SLEEP_THRESHOLD_NANOS); // increase accuracy
        }else{
            spinWaitNanos(nanos);
        }
    }

    public static void delayMillis(long millis) {
        if(millis > SLEEP_THRESHOLD_MILLIS) {
            sleepMillis(millis - SLEEP_THRESHOLD_MILLIS);
            spinWaitMillis(SLEEP_THRESHOLD_MILLIS);
        }else{
            spinWaitMillis(millis);
        }
    }

    public static void delaySeconds(double seconds) {
        final long nanos = (long) (seconds * NANOS_IN_SEC_D);
        delayNanos(nanos);
    }


    public static void waitFor(BooleanSupplier supplier) {
        while(!supplier.getAsBoolean())
            Thread.onSpinWait();
    }

    public static long waitFor(BooleanSupplier supplier, long timeoutMillis) {
        final long lastMillis = System.currentTimeMillis();
        while(!supplier.getAsBoolean() && (System.currentTimeMillis() - lastMillis) < timeoutMillis)
            Thread.onSpinWait();
        return (System.currentTimeMillis() - lastMillis);
    }

    public static long waitFor(BooleanSupplier supplier, long timeoutMillis, Runnable timeoutRunnable) {
        final long lastMillis = waitFor(supplier, timeoutMillis);
        if(!supplier.getAsBoolean())
            timeoutRunnable.run();
        return lastMillis;
    }

}
