package generaloss.chronokit;

public class TickGenerator {

    private final Sync sync;
    private volatile Thread thread;
    private volatile boolean running;

    public TickGenerator(double tickRate) {
        this.sync = new Sync(tickRate);
    }

    public Sync sync() {
        return sync;
    }

    public Thread getThread() {
        return thread;
    }

    public boolean isRunning() {
        return (running && thread != null && thread.isAlive());
    }

    public boolean isTerminated() {
        return !this.isRunning();
    }

    public void start(Runnable runnable) {
        final Thread self = Thread.currentThread();

        synchronized(this) {
            if(running && thread != self)
                throw new IllegalStateException("TickGenerator already running in another thread");

            running = true;
            thread = self;
        }

        try {
            while(!self.isInterrupted() && running) {
                try {
                    runnable.run();
                }catch(Throwable t) {
                    t.printStackTrace(System.err);
                }
                sync.sync();
            }
        }finally {
            // cleanup
            synchronized (this) {
                running = false;
                if(thread == self)
                    thread = null;
            }
        }
    }

    public synchronized Thread startAsync(Runnable runnable) {
        if(this.isRunning())
            throw new IllegalStateException("TickGenerator already running");

        thread = new Thread(() -> this.start(runnable));
        thread.setName("TickGenerator-Thread #" + this.hashCode());
        thread.setDaemon(true);

        thread.start();
        return thread;
    }

    public void stop() {
        running = false;
    }

    public void interrupt() {
        running = false;
        if(thread != null)
            thread.interrupt();
    }

}
