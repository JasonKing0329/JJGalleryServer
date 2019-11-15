package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.conf.Constants;
import com.jing.app.jjgallery.http.bean.data.FileBean;
import com.jing.app.jjgallery.http.bean.request.FolderRequest;
import com.jing.app.jjgallery.http.bean.response.FolderResponse;
import com.jing.app.jjgallery.http.HttpConstants;
import com.jing.app.jjgallery.util.ConvertUtil;

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
        if (HttpConstants.FOLDER_TYPE_ALL.equals(requestBean.getFolder())) {
            getContentList(fileList);
        } else {
            String path = requestBean.getFolder();
            File file = new File(path);
            addToFileList(file, fileList);
        }
        sendResponse(resp, responseBean);
    }

    private void getContentList(List<FileBean> fileList) {
        for (int i = 0; i < Constants.getFolderMap().length; i ++) {
            File file = new File(Constants.getFolderMap()[i][0]);
            if (file.exists()) {
                FileBean bean = new FileBean();
                bean.setFolder(true);
                bean.setName(file.getName());
                bean.setPath(file.getPath());
                bean.setLastModifyTime(file.lastModified());
                bean.setSize(getFolderSize(file));
                fileList.add(bean);
            }
        }
    }

    private void addToFileList(File folder, List<FileBean> fileList) {
        File[] files = folder.listFiles();
        for (File f : files) {
            FileBean bean = new FileBean();
            bean.setFolder(f.isDirectory());
            bean.setPath(f.getPath());
            if (f.isDirectory()) {
                bean.setName(f.getName());
                bean.setSize(getFolderSize(f));
            } else {
                String fileName = f.getName();
                if (fileName.contains(".")) {
                    String name = fileName.substring(0, fileName.lastIndexOf("."));
                    String extra = fileName.substring(fileName.lastIndexOf(".") + 1);
                    bean.setName(name);
                    bean.setExtra(extra);
                } else {
                    bean.setName(fileName);
                    bean.setExtra("");
                }

                bean.setSize(f.length());

                String url = ConvertUtil.convertUrlPath(f.getPath());
                bean.setSourceUrl(url);
            }
            bean.setLastModifyTime(f.lastModified());
            fileList.add(bean);
        }
    }

    private long getFolderSize(File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (File child : children)
                total += getFolderSize(child);
        return total;
    }
}
