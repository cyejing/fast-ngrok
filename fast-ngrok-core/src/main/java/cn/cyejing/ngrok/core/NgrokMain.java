package cn.cyejing.ngrok.core;

public class NgrokMain {

    private static final String serverAddress = "b.cye1jing.cn";
    private static final int serverPort = 4443;

    public static void main(String[] args) throws Exception {
        Tunnel tunnel = new Tunnel.TunnelBuild()
                .setPort(8080).setProto("http").setSubDomain("test").build();
        new NgrokClient(serverAddress, serverPort)
                .addTunnel(tunnel).start();

    }

}
