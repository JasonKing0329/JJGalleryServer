package com.king.service.udp;

import com.google.gson.Gson;
import com.king.desk.gdata.model.DebugLog;
import com.king.desk.gdata.model.Preference;
import com.king.desk.gdata.model.live.Observable;
import com.king.desk.gdata.model.live.ObservableEmitter;
import com.king.desk.gdata.model.live.ObservableOnSubscribe;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender {

    public Observable<Boolean> startSend() {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                try {
                    DebugLog.e("[Socket]" + "startSend");
                    DatagramSocket udpSocket = new DatagramSocket(null);
                    udpSocket.setReuseAddress(true);
                    // 本机端口与身份标识
                    String body = new Gson().toJson(new ServerBody(
                            UdpConstants.UDP_SERVER_IDENTITY, Preference.getServerPort(), Preference.getServerName()));
                    byte[] bytes = body.getBytes();
                    DatagramPacket dataPacket = new DatagramPacket(bytes, bytes.length);
                    // 全网发送
                    dataPacket.setAddress(InetAddress.getByName("255.255.255.255"));
                    // 目标端口
                    dataPacket.setPort(UdpConstants.PORT_SEND);
                    DebugLog.e("[Socket]" + "发送数据");
                    udpSocket.send(dataPacket);//发送数据
                    e.onNext(true);
                } catch (IOException exception) {
                    DebugLog.e("[socket]catch IOException " + exception.getMessage());
                    exception.printStackTrace();
                    e.onError(exception);
                }
            }
        });
    }
}
