package generaloss.chronokit;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class AsyncRunnable {

    private final Runnable runnable;
    private final AtomicReference<Future<?>> taskRef;
    private final ScheduledExecutorService executorService;

    public AsyncRunnable(Runnable runnable) {
        this.runnable = runnable;
        this.taskRef = new AtomicReference<>();
        this.executorService = Executors.newSingleThreadScheduledExecutor(r -> {
            final Thread thread = new Thread(r, "AsyncRunnable-Thread #" + this.hashCode());
            thread.setDaemon(true);
            return thread;
        });
    }


    private synchronized void submitAsyncTask(Runnable runnable) {
        if(executorService.isShutdown())
            throw new IllegalStateException("AsyncRunnable has been closed");

        final CompletableFuture<Void> future = CompletableFuture.runAsync(wrapRunnable(runnable), executorService);

        final Future<?> prev = taskRef.getAndSet(future);
        this.cancel(prev);

        future.whenComplete((result, exception) -> taskRef.compareAndSet(future, null));
    }

    private Runnable wrapRunnable(Runnable job) {
        return () -> {
            try {
                job.run();
            }catch(Throwable t) {
                t.printStackTrace(System.err);
            }
        };
    }

    private void cancel(Future<?> task) {
        if(this.isRunning(task))
            task.cancel(false);
    }

    private boolean isRunning(Future<?> task) {
        return (task != null && !task.isDone() && !task.isCancelled());
    }

    private void runDelayedSync(long delayMillis) {
        TimeUtils.sleepMillis(delayMillis);
        runnable.run();
    }

    private void runIntervalSync(long delayMillis, long periodMillis) {
        this.runDelayedSync(delayMillis);
        while(!Thread.currentThread().isInterrupted())
            this.runDelayedSync(periodMillis);
    }

    public synchronized void runDelayed(long delayMillis) {
        this.submitAsyncTask(() -> this.runDelayedSync(delayMillis));
    }

    public synchronized void runInterval(long delayMillis, long periodMillis) {
        this.submitAsyncTask(() -> this.runIntervalSync(delayMillis, periodMillis));
    }


    public synchronized void cancel() {
        this.cancel(taskRef.get());
    }

    public synchronized boolean isRunning() {
        return this.isRunning(taskRef.get());
    }

    public boolean isTerminated() {
        return !this.isRunning();
    }

    public synchronized void close() {
        this.cancel();
        executorService.shutdown();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

}