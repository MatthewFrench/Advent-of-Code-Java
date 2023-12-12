package Advent.of.Code.Java.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleParallelism {
    private final ExecutorService executorService;
    private final List<CompletableFuture<Void>> futures;

    public SimpleParallelism() {
        executorService = Executors.newCachedThreadPool();
        futures = new ArrayList<>();
    }
    public void add(Runnable runnable) {
        futures.add(CompletableFuture.runAsync(runnable, executorService));
    }
    public void waitForCompletion() {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executorService.shutdown();
    }
}
