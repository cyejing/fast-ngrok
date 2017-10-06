package cn.cyejing.ngrok.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ngrok")
public class NgrokProperties {

    private String serverAddress = "b.cyejing.cn";
    private int serverPort = 4443;

    private String proto = "http";
    private String subdomain;
    private String hostname;
    private int remotePort;
    private String httpAuth;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getHttpAuth() {
        return httpAuth;
    }

    public void setHttpAuth(String httpAuth) {
        this.httpAuth = httpAuth;
    }
}
