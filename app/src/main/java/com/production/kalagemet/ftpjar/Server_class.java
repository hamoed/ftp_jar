package com.production.kalagemet.ftpjar;

public class Server_class {
    public static String ip;
    public static int port;

    public void setIp(String ip) {
        Server_class.ip = ip;
    }

    public void setPort(int port) {
        Server_class.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
