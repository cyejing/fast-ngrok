package cn.cyejing.ngrok.autoconfigure;

import cn.cyejing.ngrok.core.NgrokClient;
import cn.cyejing.ngrok.core.Tunnel;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;

public class EmbeddedServletContainerInitializedEventListener implements
        ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        int port = event.getSource().getPort();

        Tunnel tunnel = new Tunnel.TunnelBuild()
                .setPort(port).setProto("http").build();
        new NgrokClient("b.cyejing.cn", 4443)
                .addTunnel(tunnel).start();
    }
}

