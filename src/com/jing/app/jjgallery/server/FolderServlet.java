package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.conf.Constants;
import com.jing.app.jjgallery.http.HttpConstants;
import com.jing.app.jjgallery.http.bean.data.FileBean;
import com.jing.app.jjgallery.http.bean.request.FolderRequest;
import com.jing.app.jjgallery.http.bean.response.FolderResponse;
import com.jing.app.jjgallery.model.PathBean;
import com.jing.app.jjgallery.model.parser.PathContextParser;
import com.jing.app.jjgallery.model.parser.TvConfParser;
import com.jing.app.jjgallery.util.ConvertUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
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
            getContentList(requestBean.isGuest(), fileList, requestBean.isCountSize());
        } else {
            String path = requestBean.getFolder();
            File file = new File(path);
            addToFileList(requestBean.isGuest(), file, fileList, requestBean.getFilterType(), requestBean.isCountSize());
        }
        sendResponse(resp, responseBean);
    }

    private void getContentList(boolean isGuest, List<FileBean> fileList, boolean countSize) {
        List<PathBean> list = PathContextParser.getInstance().getPathList();
        if (list != null) {
            for (int i = 0; i < list.size(); i ++) {
                String docBase = list.get(i).getDocBase();
                // 访客模式下验证是否是su目录
                if (isGuest && isPathForSu(docBase)) {
                    continue;
                }
                File file = new File(docBase);
                if (file.exists()) {
                    FileBean bean = new FileBean();
                    bean.setFolder(true);
                    bean.setName(file.getName());
                    bean.setPath(file.getPath());
                    bean.setLastModifyTime(file.lastModified());
                    if (countSize) {
                        bean.setSize(getFolderSize(file));
                    }
                    fileList.add(bean);
                }
            }
        }
    }

    private boolean isPathForSu(String docBase) {
        docBase = docBase.replaceAll("\\\\", "/");
        List<String> suList = TvConfParser.getInstance().getSuPathList();
        for (String path:suList) {
            if (docBase.startsWith(path)) {
                return true;
            }
        }
        return false;
    }

    private class VideoFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            boolean result = false;
            if (file.isDirectory()) {
                result = true;
            }
            else {
                for (String type: Constants.videoTypes) {
                    if (file.getName().endsWith(type)) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }
    }

    private void addToFileList(boolean isGuest, File folder, List<FileBean> fileList, int filterType, boolean countSize) {
        File[] files;
        if (filterType == HttpConstants.FILE_FILTER_VIDEO) {
            files = folder.listFiles(new VideoFilter());
        }
        else {
            files = folder.listFiles();
        }
        for (File f : files) {
            // 访客模式下验证是否是su目录
            if (isGuest && f.isDirectory() && isPathForSu(f.getPath())) {
                continue;
            }
            FileBean bean = new FileBean();
            bean.setFolder(f.isDirectory());
            bean.setPath(f.getPath());
            if (f.isDirectory()) {
                bean.setName(f.getName());
                if (countSize) {
                    bean.setSize(getFolderSize(f));
                }
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
