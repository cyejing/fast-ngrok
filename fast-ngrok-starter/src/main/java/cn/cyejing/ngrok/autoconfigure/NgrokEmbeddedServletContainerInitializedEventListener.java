package cn.cyejing.ngrok.autoconfigure;

import cn.cyejing.ngrok.core.NgrokClient;
import cn.cyejing.ngrok.core.Tunnel;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;

public class NgrokEmbeddedServletContainerInitializedEventListener implements
        ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    private final NgrokProperties ngrokProperties;

    public NgrokEmbeddedServletContainerInitializedEventListener(NgrokProperties ngrokProperties) {
        this.ngrokProperties = ngrokProperties;
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        int port = event.getSource().getPort();
        String serverAddress = ngrokProperties.getServerAddress();
        int serverPort = ngrokProperties.getServerPort();
        String subdomain = ngrokProperties.getSubdomain();
        String hostname = ngrokProperties.getHostname();
        String proto = ngrokProperties.getProto();
        int remotePort = ngrokProperties.getRemotePort();
        String httpAuth = ngrokProperties.getHttpAuth();

        Tunnel tunnel = new Tunnel.TunnelBuild()
                .setPort(port)
                .setProto(proto)
                .setSubDomain(subdomain)
                .setHostname(hostname)
                .setRemotePort(remotePort)
                .setHttpAuth(httpAuth)
                .build();
        new NgrokClient(serverAddress, serverPort)
                .addTunnel(tunnel).start();
    }
}

