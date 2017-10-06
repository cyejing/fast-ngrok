package cn.cyejing.ngrok.core.woker;

import cn.cyejing.ngrok.core.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用ping维持链接正常
 */
public class HealthCheckWorker implements Runnable {
    Logger log = LoggerFactory.getLogger(HealthCheckWorker.class);

    private final MessageHandler messageHandler;

    public HealthCheckWorker(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        while (true) {
            try {
                messageHandler.sendPing();
                Thread.sleep(30000);
            } catch (Exception e) {
                log.error("occurred some exception", e);
            }
        }
    }
}
