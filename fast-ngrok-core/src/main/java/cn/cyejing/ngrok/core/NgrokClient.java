package cn.cyejing.ngrok.core;

import cn.cyejing.ngrok.core.woker.MessageListenerWorker;

import javax.net.ssl.SSLSocket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 启动客户端
 */
public class NgrokClient {

    private final SSLSocket socket;
    private final SocketFactory socketFactory;
    private List<Tunnel> tunnels = new ArrayList<>();

    public NgrokClient(String serverAddress, int serverPort) {
        this.socketFactory = new SocketFactory(serverAddress, serverPort);
        this.socket = this.socketFactory.build();
    }

    public void start() {
        MessageHandler messageHandler = new MessageHandler(socket, socketFactory, this.tunnels);
        new Thread(new MessageListenerWorker(messageHandler)).start();
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
