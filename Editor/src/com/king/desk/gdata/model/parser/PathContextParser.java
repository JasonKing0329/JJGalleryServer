package com.king.desk.gdata.model.parser;

import com.king.desk.gdata.model.bean.PathBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathContextParser extends XmlParser {

    private List<PathBean> pathList;

    private static PathContextParser instance;

    public static PathContextParser getInstance() {
        if (instance == null) {
            instance = new PathContextParser();
        }
        return instance;
    }

    private PathContextParser() {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public List<PathBean> getPathList() {
        return pathList;
    }

    @Override
    protected File getFile() {
        return new File("C:\\Program Files\\apache-tomcat-7.0.70\\conf\\server.xml");
    }

    @Override
    protected void doParse(Document parse) {
        pathList = new ArrayList<>();
        Node host = parse.getElementsByTagName("Host").item(0);
        NodeList children = host.getChildNodes();
        for (int i = 0; i < children.getLength(); i ++) {
            Node node = children.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if ("Context".equals(node.getNodeName())) {
                    String path = ele.getAttribute("path");
                    if (path.startsWith("/JJGalleryServer/videos")) {
                        PathBean bean = new PathBean();
                        bean.setPath(path);
                        bean.setDocBase(ele.getAttribute("docBase").replaceAll("\\\\", "/"));
                        pathList.add(bean);
                    }
                }
            }
        }
    }
}
