package cn.cyejing.ngrok.core;

import cn.cyejing.ngrok.core.woker.MessageListenerWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 启动客户端
 */
public class NgrokClient {
    private static Logger log = LoggerFactory.getLogger(NgrokClient.class);

    private SSLSocket socket;
    private final SocketFactory socketFactory;
    private List<Tunnel> tunnels = new ArrayList<>();

    public NgrokClient(String serverAddress, int serverPort) {
        this.socketFactory = new SocketFactory(serverAddress, serverPort);
    }

    public void start() {
        try {
            this.socket = this.socketFactory.build();
        } catch (NgrokClientException e) {
            log.error("Ngrok Start failed: ", e);
            return;
        }

        MessageHandler messageHandler = new MessageHandler(socket, socketFactory, this.tunnels);
        WorkerPool.submit(new MessageListenerWorker(messageHandler));
        messageHandler.sendAuth();

    }

    public List<Tunnel> getTunnels() {
        return this.tunnels;
    }

    public NgrokClient addTunnel(Tunnel tunnel) {
        this.tunnels.add(tunnel);
        return this;
    }
}
