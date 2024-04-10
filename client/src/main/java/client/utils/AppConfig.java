package client.utils;

public class AppConfig {
    private String address;
    private String websocket;
    private int port;
    private MailConfig mailConfig;

    public AppConfig(String address, String websocket, int port, MailConfig mailConfig) {
        this.address = address;
        this.websocket = websocket;
        this.port = port;
        this.mailConfig = mailConfig;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsocket() {
        return websocket;
    }

    public int getPort() {
        return port;
    }

    public MailConfig getMailConfig() {
        return mailConfig;
    }
}
