package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.bean.http.FileBean;
import com.jing.app.jjgallery.bean.http.FolderRequest;
import com.jing.app.jjgallery.bean.http.FolderResponse;
import com.jing.app.jjgallery.conf.Configuration;
import com.jing.app.jjgallery.conf.HttpConstants;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * servlet to handle folder surf action
 */
public class FolderServlet extends BaseJsonServlet<FolderRequest, FolderResponse> {

    @Override
    protected Class<FolderRequest> getRequestClass() {
        return FolderRequest.class;
    }

    @Override
    protected void onReceiveRequest(FolderRequest requestBean, HttpServletResponse resp) throws IOException {
        if (requestBean == null) {
            return;
        }
        FolderResponse responseBean = new FolderResponse();
        responseBean.setType(requestBean.getType());
        List<FileBean> fileList = new ArrayList<>();
        responseBean.setFileList(fileList);
        if (HttpConstants.FOLDER_TYPE_CONTENT.equals(requestBean.getType())) {
            getContentList(fileList);
        }
        else if (HttpConstants.FOLDER_TYPE_ALL.equals(requestBean.getType())) {
            File file = new File(Configuration.getVideoScenePath(getServletContext()));
            addToFileList(file, fileList);
            file = new File(Configuration.getVideoStarPathE(getServletContext()));
            addToFileList(file, fileList);
            file = new File(Configuration.getVideoStarPathD(getServletContext()));
            addToFileList(file, fileList);
        }
        else if (HttpConstants.FOLDER_TYPE_SCENE.equals(requestBean.getType())) {
            String path = Configuration.getVideoScenePath(getServletContext());
            if (requestBean.getFolder() != null) {
                path = path + "/" + requestBean.getFolder();
            }
            File file = new File(path);
            addToFileList(file, fileList);
        }
        else if (HttpConstants.FOLDER_TYPE_STAR.equals(requestBean.getType())) {
            String path;
            if (requestBean.getFolder() != null) {
                char index = requestBean.getFolder().toLowerCase().charAt(0);
                if (String.valueOf(index).compareTo(Configuration.getVideoStarPathDIndex(getServletContext())) >= 0) {
                    path = Configuration.getVideoStarPathD(getServletContext()) + "/" + requestBean.getFolder();
                }
                else {
                    path = Configuration.getVideoStarPathE(getServletContext()) + "/" + requestBean.getFolder();
                }
                File file = new File(path);
                addToFileList(file, fileList);
            }
            else {
                File file = new File(Configuration.getVideoStarPathE(getServletContext()));
                addToFileList(file, fileList);
                file = new File(Configuration.getVideoStarPathD(getServletContext()));
                addToFileList(file, fileList);
            }
        }
        sendResponse(resp, responseBean);
    }

    private void getContentList(List<FileBean> fileList) {
        FileBean bean = new FileBean();
        bean.setFolder(true);
        bean.setName(HttpConstants.FOLDER_TYPE_ALL);
        bean.setPath(HttpConstants.FOLDER_TYPE_ALL);
        fileList.add(bean);

        File file = new File(Configuration.getVideoScenePath(getServletContext()));
        bean = new FileBean();
        bean.setFolder(true);
        bean.setName(file.getName());
        bean.setPath(file.getPath());
        fileList.add(bean);

        file = new File(Configuration.getVideoStarPathE(getServletContext()));
        bean = new FileBean();
        bean.setFolder(true);
        bean.setName(file.getName());
        bean.setPath(file.getPath());
        fileList.add(bean);

        file = new File(Configuration.getVideoStarPathD(getServletContext()));
        bean = new FileBean();
        bean.setFolder(true);
        bean.setName(file.getName());
        bean.setPath(file.getPath());
        fileList.add(bean);
    }

    private void addToFileList(File folder, List<FileBean> fileList) {
        File[] files = folder.listFiles();
        for (File f:files) {
            FileBean bean = new FileBean();
            bean.setFolder(f.isDirectory());
            bean.setPath(f.getPath());
            if (f.isDirectory()) {
                bean.setName(f.getName());
            }
            else {
                String fileName = f.getName();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                String extra = fileName.substring(fileName.lastIndexOf(".") + 1);
                bean.setName(name);
                bean.setExtra(extra);
            }
            fileList.add(bean);
        }
    }
}
