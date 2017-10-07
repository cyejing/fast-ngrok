package cn.cyejing.ngrok.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerPool {
    private static Logger log = LoggerFactory.getLogger(WorkerPool.class);
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private static final AtomicInteger num = new AtomicInteger(1);

    public static void shutdown() {
        log.info("Ngrok Worker is shutdown!");
        executorService.shutdownNow();
    }

    public static void submit(Runnable worker) {
        executorService.submit(worker);
//        new Thread(worker, worker.getClass().getSimpleName() + "-" + num.getAndIncrement()).start();
    }


}
