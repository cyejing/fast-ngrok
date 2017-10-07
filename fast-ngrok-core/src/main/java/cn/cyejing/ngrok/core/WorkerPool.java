package cn.cyejing.ngrok.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerPool {
    private static Logger log = LoggerFactory.getLogger(WorkerPool.class);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void shutdown() {
        log.info("Ngrok Worker is shutdown!");
        executorService.shutdownNow();
    }

    public static void submit(Runnable worker) {
        executorService.submit(worker);
    }


}
