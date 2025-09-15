# [Chrono Kit](https://github.com/generaloss/chrono-kit)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.generaloss/chrono-kit.svg)](https://mvnrepository.com/artifact/io.github.generaloss/chrono-kit)

---

### TimeUtils

``` java
TimeUtils.delayNanos(long nanos);
TimeUtils.delayMillis(long millis);
TimeUtils.delaySeconds(long seconds);

TimeUtils.waitFor(BooleanSupplier supplier);
long timeMillis = TimeUtils.waitFor(BooleanSupplier supplier, long timeoutMillis);
long timeMillis = TimeUtils.waitFor(BooleanSupplier supplier, long timeoutMillis, Runnable timeoutRunnable);
// and more
```

### Stopwatch

showcases:

``` java
Stopwatch stopwatch = new Stopwatch().start();
// 1-second task
final float seconds = stopwatch.getSeconds(); // ~ 1f
```

``` java
Stopwatch stopwatch = new Stopwatch().start();
// 3-second task
final float stamp1 = stopwatch.getSeconds(); // ~ 3f
// 300-ms task
final float stamp2 = stopwatch.getMillis(); // ~ 3300f
```

``` java
Stopwatch stopwatch = new Stopwatch().start();
// 5-second task
final float seconds = stopwatch.getSeconds(); // ~ 5f

stopwatch.reset();
// 500-ms task
final float millis = stopwatch.getMillis(); // ~ 500f
```

### Sync

``` java
Sync sync = new Sync(20.0); // enabled by default

while(true) {
    sync.sync();
    // do something 20 times per second
}
```

### PerSecCounter

``` java
PerSecCounter fpsCounter = new PerSecCounter();

while(true) {
    fpsCounter.update();
    int fps = fpsCounter.getCount();
    
    ...
}
```

### DeltaTimeCounter

``` java
DeltaTimeCounter dtCounter = new DeltaTimeCounter();

while(true) {
    dtCounter.update();
    float deltaTime = dtCounter.getTime();
    
    ...
}
```

### TickGenerator

``` java
final TickGenerator tickGen = new TickGenerator(20.0);

tickGen.startAsync(() -> {
    // do something 20 times per second
    // in new thread
});
tickGen.start(() -> {
    // in current thread
});
```

### AsyncRunnable

``` java 
AsyncRunnable runnable = new AsyncRunnable(() -> {
    ...
});

runnable.runDelayed(2000L); // run after 2 seconds delay
runnable.runInterval(3000L); // runs every 3 seconds
runnable.runInterval(1000L, 3000L); // second delay + runs every 3 seconds

...
runnable.close(); // or cancel() or awaitTermination(long timeout, TimeUnit unit)
```