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
        // This assumes that it is better to run one item at a time before grabbing the next, to use the max cpu
        // and that all the items are calculation heavy.
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
