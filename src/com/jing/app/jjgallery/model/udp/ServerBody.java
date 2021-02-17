package com.jing.app.jjgallery.model.udp;

/**
 * Desc:本机端口与身份标识，通过UDP广播
 *
 * @author：Jing Yang
 * @date: 2020/4/28 18:10
 */
public class ServerBody {

    private String identity;
    private int port;
    private String extraUrl;
    private String serverName;

    public ServerBody(String identity, int port, String extraUrl, String serverName) {
        this.identity = identity;
        this.port = port;
        this.extraUrl = extraUrl;
        this.serverName = serverName;
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

    public String getExtraUrl() {
        return extraUrl;
    }

    public void setExtraUrl(String extraUrl) {
        this.extraUrl = extraUrl;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
