package com.king.service.udp;

/**
 * Desc:本机端口与身份标识，通过UDP广播
 *
 * @author：Jing Yang
 * @date: 2020/4/28 18:10
 */
public class ServerBody {

    private String identity;
    private int port;

    public ServerBody(String identity, int port) {
        this.identity = identity;
        this.port = port;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
