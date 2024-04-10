package client.utils;

import java.util.Objects;

public class MailConfig {
    private String username;
    private String password;
    private String host;
    private int port;
    private boolean smtpAuth;
    private boolean startTls;

    public MailConfig(String username, String password, String host, int port, boolean smtpAuth, boolean startTls) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.smtpAuth = smtpAuth;
        this.startTls = startTls;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public boolean isStartTls() {
        return startTls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailConfig that = (MailConfig) o;
        return port == that.port && smtpAuth == that.smtpAuth && startTls == that.startTls && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, host, port, smtpAuth, startTls);
    }

}
