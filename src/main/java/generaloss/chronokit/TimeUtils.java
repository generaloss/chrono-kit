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


    public static void sleep(long millis, int nanos) {
        try{
            Thread.sleep(millis, nanos);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public static void sleepMillis(long millis) {
        try{
            Thread.sleep(millis);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public static void sleepNanos(long nanos) {
        if(nanos <= 0)
            return;

        final long millis = (nanos / NANOS_IN_MS_L);
        final int nanosPart = (int) (nanos % NANOS_IN_MS_L); // [0 .. 999_999]

        try{
            Thread.sleep(millis, nanosPart);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public static void sleepSeconds(double seconds) {
        if(seconds <= 0.0)
            return;

        final long nanos = (long) (seconds * NANOS_IN_SEC_D);
        sleepNanos(nanos);
    }


    public static void delayNanos(long nanos) {
        final long current = System.nanoTime();
        while(System.nanoTime() - current < nanos)
            Thread.onSpinWait();
    }

    public static void delayMillis(long millis) {
        final long current = System.currentTimeMillis();
        while(System.currentTimeMillis() - current < millis)
            Thread.onSpinWait();
    }

    public static void delaySeconds(double seconds) {
        delayMillis((long) (seconds * 1000D));
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

    public static long waitFor(BooleanSupplier supplier, long timeoutMillis, Runnable onTimeout) {
        final long lastMillis = waitFor(supplier, timeoutMillis);
        if(!supplier.getAsBoolean())
            onTimeout.run();
        return lastMillis;
    }

}
