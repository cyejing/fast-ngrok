package cn.cyejing.ngrok.core;

import cn.cyejing.ngrok.core.woker.HealthCheckWorker;
import cn.cyejing.ngrok.core.woker.MessageListenerWorker;
import cn.cyejing.ngrok.core.woker.SocketSwapWorker;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.runtime.WithObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 消息处理器
 */
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private SSLSocket socket;
    private SocketFactory socketFactory;
    private String clientId;
    private List<Tunnel> tunnels;
    private Map<String, Tunnel> mappingMap = new HashMap<>();


    public MessageHandler(SSLSocket socket, SocketFactory socketFactory, List<Tunnel> tunnels) {
        this.socket = socket;
        this.socketFactory = socketFactory;
        this.tunnels = tunnels;
    }

    /**
     * 处理服务器返回的消息
     *
     * @param json
     */
    public boolean onMessage(JSONObject json) throws Exception {
        String type = json.getString("Type");
        JSONObject payload = json.getJSONObject("Payload");
        switch (type) {
            case "AuthResp": {
                String clientId = payload.getString("ClientId");
                this.clientId = clientId;
                String error = payload.getString("Error");
                if (StringUtils.isBlank(error)) {
                    log.debug("auth succeed...");
                    sendReqTunnel();
                    WorkerPool.submit(new HealthCheckWorker(this));
                } else {
                    WorkerPool.shutdown();
                    log.error("auth failed error: {}", error);
                }
                break;
            }
            case "NewTunnel": {
                String error = payload.getString("Error");
                String reqId = payload.getString("ReqId");
                String url = payload.getString("Url");
                if (StringUtils.isBlank(error)) {
                    mappingMap.put(url, mappingMap.get(reqId));
                    log.info("register url: {}", url);
                } else {
                    WorkerPool.shutdown();
                    log.error("NewTunnel failed error: {}", error);
                }
                break;
            }
            case "ReqProxy":
                //注册代理需要新的线程和连接
                MessageHandler messageHandler = newSocketAndCopy();
                messageHandler.sendRegProxy();
                WorkerPool.submit(new MessageListenerWorker(messageHandler));
                break;
            case "StartProxy": {
                String url = payload.getString("Url");
                Tunnel tunnel = mappingMap.get(url);

                Socket locals = new Socket("127.0.0.1", Integer.valueOf(tunnel.getPort()));
                WorkerPool.submit(new SocketSwapWorker(this.socket.getInputStream(), locals.getOutputStream()));
                WorkerPool.submit(new SocketSwapWorker(locals.getInputStream(), this.socket.getOutputStream()));

                return true;
            }
        }
        return false;
    }

    private MessageHandler newSocketAndCopy() {
        SSLSocket newSocket = socketFactory.build();
        MessageHandler messageHandler = new MessageHandler(newSocket, socketFactory, null);
        messageHandler.setClientId(this.clientId);
        messageHandler.setMappingMap(this.mappingMap);
        return messageHandler;
    }

    /**
     * 发送认证授权
     */
    public void sendAuth() {
        JSONObject request = new JSONObject();
        request.put("Type", "Auth");
        JSONObject payload = new JSONObject();
        payload.put("Version", "2");
        payload.put("MmVersion", "1.7");
        payload.put("User", "");
        payload.put("Password", "");
        payload.put("OS", "darwin");
        payload.put("Arch", "amd64");
        payload.put("ClientId", "");
        request.put("Payload", payload);
        sendMessage(request.toJSONString());
    }

    public void sendReqTunnel() {
        for (Tunnel tunnel : this.tunnels) {
            JSONObject reuqest = new JSONObject();
            reuqest.put("Type", "ReqTunnel");

            JSONObject payload = new JSONObject();
            String reqId = UUID.randomUUID().toString()
                    .toLowerCase().replace("-", "")
                    .substring(0, 16);
            mappingMap.put(reqId, tunnel);
            payload.put("ReqId", reqId);
            payload.put("Protocol", tunnel.getProto());
            if (tunnel.getProto().equals("tcp")) {
                payload.put("RemotePort", tunnel.getRemotePort());
            } else {
                payload.put("Subdomain", tunnel.getSubDomain());
                payload.put("HttpAuth", tunnel.getHttpAuth());
                payload.put("Hostname", tunnel.getHostname());
            }
            reuqest.put("Payload", payload);
            sendMessage(reuqest.toJSONString());
        }
    }

    public void sendPong() {
        sendMessage("{\"Type\":\"Pong\",\"Payload\":{}}");
    }

    public void sendPing() {
        sendMessage("{\"Type\":\"Ping\",\"Payload\":{}}");
    }


    public void sendRegProxy() {
        sendMessage("{\"Type\":\"RegProxy\",\"Payload\":{\"ClientId\":\"" + clientId + "\"}}");
    }

    public void sendMessage(String str) {
        log.debug("Writing message: {}", str);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(this.socket.getOutputStream());
            byte[] len = ByteBuffer.allocate(8).putLong(str.length()).array();
            ArrayUtils.reverse(len);
            ByteBuffer wrap = ByteBuffer.allocate(str.length() + 8);
            byte[] array = wrap.put(len).put(str.getBytes()).array();

            bos.write(array);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SSLSocket getSocket() {
        return socket;
    }

    public void setSocket(SSLSocket socket) {
        this.socket = socket;
    }

    public SocketFactory getSocketFactory() {
        return socketFactory;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<Tunnel> getTunnels() {
        return tunnels;
    }

    public void setTunnels(List<Tunnel> tunnels) {
        this.tunnels = tunnels;
    }

    public Map<String, Tunnel> getMappingMap() {
        return mappingMap;
    }

    public void setMappingMap(Map<String, Tunnel> mappingMap) {
        this.mappingMap = mappingMap;
    }
}
