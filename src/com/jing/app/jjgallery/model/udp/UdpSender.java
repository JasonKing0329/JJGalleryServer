package com.jing.app.jjgallery.model.udp;

import com.google.gson.Gson;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 每10秒广播一次本机IP及标识
 */
public class UdpSender {

    private boolean isRun = true;

    private Thread thread;

    public void start(String serverName, int udpTime) {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("[Socket]" + "startSend");
                    DatagramSocket udpSocket = new DatagramSocket(null);
                    udpSocket.setReuseAddress(true);
                    // 本机端口与身份标识
                    String body = new Gson().toJson(
                            new ServerBody(
                                    UdpConstants.UDP_SERVER_IDENTITY,
                                    8080,
                                    "JJGalleryServer",
                                    serverName
                            )
                    );

                    byte[] bytes = body.getBytes();
                    DatagramPacket dataPacket = new DatagramPacket(bytes, bytes.length);
                    // 全网发送
                    dataPacket.setAddress(InetAddress.getByName("255.255.255.255"));
                    // 目标端口
                    dataPacket.setPort(UdpConstants.PORT_SEND);

                    while (isRun) {
                        System.out.println("[Socket]" + "发送数据:" + body);
                        udpSocket.send(dataPacket);//发送数据
                        Thread.sleep(udpTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void destroy() {
        isRun = false;
        thread.interrupt();
    }
}
