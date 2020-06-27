package com.king.service.udp;

import com.google.gson.Gson;
import com.king.desk.gdata.model.DebugLog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender {

    public void startSend() {
        new Thread() {
            public void run() {
                try {
                    DebugLog.e("[Socket]" + "startSend");
                    DatagramSocket udpSocket = new DatagramSocket(null);
                    udpSocket.setReuseAddress(true);
                    // 本机端口与身份标识
                    String body = new Gson().toJson(new ServerBody(UdpConstants.UDP_SERVER_IDENTITY, UdpConstants.PORT_SERVER));
                    byte[] bytes = body.getBytes();
                    DatagramPacket dataPacket = new DatagramPacket(bytes, bytes.length);
                    // 全网发送
                    dataPacket.setAddress(InetAddress.getByName("255.255.255.255"));
                    // 目标端口
                    dataPacket.setPort(UdpConstants.PORT_SEND);
                    DebugLog.e("[Socket]" + "发送数据");
                    udpSocket.send(dataPacket);//发送数据
                } catch (IOException e) {
                    DebugLog.e("[socket]catch IOException " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
