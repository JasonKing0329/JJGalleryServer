package com.jing.app.jjgallery.server;

import com.jing.app.jjgallery.http.bean.data.FileBean;
import com.jing.app.jjgallery.http.bean.request.SubtitleRequest;
import com.jing.app.jjgallery.http.bean.response.SubtitleResponse;
import com.jing.app.jjgallery.util.ConvertUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * servlet to handle folder surf action
 */
public class SubtitleServlet extends BaseJsonServlet<SubtitleRequest, SubtitleResponse> {

    @Override
    protected Class<SubtitleRequest> getRequestClass() {
        return SubtitleRequest.class;
    }

    @Override
    protected void onReceiveRequest(SubtitleRequest requestBean, HttpServletResponse resp) throws IOException {
        if (requestBean == null) {
            return;
        }

        SubtitleResponse responseBean = new SubtitleResponse();
        File file = new File(requestBean.getFilePath());
        if (file.exists()) {
            String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            // 寻找字幕文件夹
            File parent = file.getParentFile();
            File subtitleFolder = new File(parent.getPath() + "/" + fileName);
            if (subtitleFolder.exists() && subtitleFolder.isDirectory()) {
                responseBean.setFileList(new ArrayList<>());
                addToFileList(subtitleFolder, responseBean.getFileList());
            }
        }

        sendResponse(resp, responseBean);
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
