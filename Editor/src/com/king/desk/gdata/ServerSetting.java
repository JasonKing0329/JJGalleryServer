package com.king.desk.gdata;

import com.king.desk.gdata.model.Preference;
import com.king.desk.gdata.model.live.Observer;
import com.king.service.udp.UdpSender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerSetting extends BaseDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField etPort;
    private JTextField etServerName;
    private JButton btnUdp;

    public ServerSetting() {
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateSizeAndLocation();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                updateSizeAndLocation();
            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        btnUdp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendUdp();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        etPort.setText(String.valueOf(Preference.getServerPort()));
        etServerName.setText(Preference.getServerName());
    }

    private void sendUdp() {
        // 发送udp广播，向client告知server的IP及端口
        new UdpSender().startSend()
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(Boolean value) {
                        showConfirm("发送成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConfirm("发送失败 " + e.getMessage());
                    }
                });
    }

    private void updateSizeAndLocation() {
        Rectangle bounds = getBounds();
        Point locations = getLocation();
        Rectangle frame = new Rectangle();
        frame.setBounds(locations.x, locations.y, bounds.width, bounds.height);
        Preference.setServerSettingFrame(frame);
    }

    @Override
    protected void onShow() {
        setTitle("服务器配置");
    }

    @Override
    protected Rectangle customInitSize() {
        return Preference.getServerSettingFrame();
    }

    private void onOK() {
        try {
            Preference.setServerPort(Integer.parseInt(etPort.getText()));
        } catch (Exception e) {
            showConfirm("端口号输入有误");
            return;
        }
        Preference.setServerName(etServerName.getText());
        showConfirm("保存成功");
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Preference.createIfNotInit();
        ServerSetting dialog = new ServerSetting();
        dialog.showDialog();
    }
}
