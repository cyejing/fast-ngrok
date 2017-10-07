package cn.cyejing.ngrok.core.woker;

import cn.cyejing.ngrok.core.MessageHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocket;
import java.io.BufferedInputStream;
import java.nio.ByteBuffer;

/**
 * 监听消息
 */
public class MessageListenerWorker implements Runnable {
    Logger log = LoggerFactory.getLogger(MessageListenerWorker.class);

    private final SSLSocket socket;
    private final MessageHandler messageHandler;

    public MessageListenerWorker(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.socket = messageHandler.getSocket();
    }

    @Override
    public void run() {
        try {
            log.debug("Waiting to read message");
            byte[] hLen = new byte[8];
            byte[] strByte;
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            while (true) {
                while (bis.available() >= 8) {

                }
                int i = bis.read(hLen);
                if (i == -1) {
                    return;
                }

                ArrayUtils.reverse(hLen);
                int strLen = ((Long) ByteBuffer
                        .wrap(hLen).getLong())
                        .intValue();

                log.debug("Reading message with length: {}", strLen);
                strByte = new byte[strLen];
                int readCount = 0;
                while (readCount < strLen) {
                    int read = bis.read(strByte, readCount, strLen - readCount);
                    if (read == -1) {
                        return;
                    }
                    readCount += read;
                }
                JSONObject json = JSON.parseObject(strByte, JSONObject.class);
                log.debug("Read message: {}", json.toJSONString());
                if (messageHandler.onMessage(json)) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Occurred some exception", e);
        }

    }
}
