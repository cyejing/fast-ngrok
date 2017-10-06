package cn.cyejing.ngrok.core;

public class NgrokMain {

    private static final String serverAddress = "b.cyejing.cn";
    private static final int serverPort = 4443;

    public static void main(String[] args) throws Exception {
        Tunnel tunnel = new Tunnel.TunnelBuild()
                .setPort(8080).setProto("http").build();
        new NgrokClient(serverAddress, serverPort)
                .addTunnel(tunnel).start();

    }

}
