package com.jing.app.jjgallery.model.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TvConfParser extends XmlParser {

    private List<String> suPathList = new ArrayList<>();

    private static TvConfParser instance;

    private File xmlFile;

    public static TvConfParser getInstance() {
        if (instance == null) {
            instance = new TvConfParser();
        }
        return instance;
    }

    private TvConfParser() {
    }

    public void init() {
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

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public List<String> getSuPathList() {
        return suPathList;
    }

    @Override
    protected File getFile() {
        return xmlFile;
    }

    @Override
    protected void doParse(Document parse) {
        Node pathAuth = parse.getElementsByTagName("Path_Auth").item(0);
        NodeList pathChildren = pathAuth.getChildNodes();
        for (int i = 0; i < pathChildren.getLength(); i ++) {
            Node node = pathChildren.item(i);
            String node_name = node.getNodeName();
            if ("SU".equals(node_name)) {
                NodeList items = node.getChildNodes();
                for (int j = 0; j < items.getLength(); j ++) {
                    Node item = items.item(j);
                    if ("Item".equals(item.getNodeName())) {
                        String path = item.getTextContent();
                        suPathList.add(path);
                    }
                }
            }
        }
    }
}
