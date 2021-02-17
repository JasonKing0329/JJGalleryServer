package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.conf.SettingProperties;
import com.jing.app.jjgallery.model.parser.PathContextParser;
import com.jing.app.jjgallery.model.parser.TvConfParser;
import com.jing.app.jjgallery.model.udp.UdpSender;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class TomcatListener implements ServletContextListener {

    private UdpSender udpSender = new UdpSender();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println(getClass().getName() + "[contextInitialized]");
        long start = System.currentTimeMillis();
        // 广播本机IP与身份标识
        String serverName = SettingProperties.getServerName(servletContextEvent.getServletContext());
        int udpTime = SettingProperties.getUdpTime(servletContextEvent.getServletContext());
        udpSender.start(serverName, udpTime);
        // server.xml数据
        File server = new File(SettingProperties.getServerXmlPath(servletContextEvent.getServletContext()));
        if (server.exists()) {
            PathContextParser.getInstance().setXmlFile(server);
            PathContextParser.getInstance().init();
        }
        // conf_tv.xml数据
        File confTv = new File(SettingProperties.getConfTvXmlPath(servletContextEvent.getServletContext()));
        if (confTv.exists()) {
            TvConfParser.getInstance().setXmlFile(confTv);
            TvConfParser.getInstance().init();
        }
        long end = System.currentTimeMillis();
        System.out.println(getClass().getName() + "[contextInitialized] cost time " + (end - start));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println(getClass().getName() + "[contextDestroyed]");
        udpSender.destroy();
    }

}
