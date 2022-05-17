package com.xhtech.tool.jenkins.service;

import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.util.StrUtil;
import com.xhtech.tool.jenkins.constant.Constants;
import com.xhtech.tool.jenkins.util.LogUtil;
import hudson.FilePath;
import hudson.model.TaskListener;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class UploadFileService {
    /**
     * 类型包括：aliyun/minio，默认aliyun
     */
    private String ossType;

    /**
     * 默认：xh-res
     */
    private String bucketName;

    /**
     * 目标文件的目录
     */
    private String targetPath;

    private boolean debug;

    public UploadFileService(String ossType, String bucketName, String targetPath, boolean debug) {
        this.ossType = StrUtil.isEmpty(ossType) ? Constants.OssType.ALIYUN : ossType;
        this.bucketName = StrUtil.isEmpty(bucketName) ? Constants.DEFAULT_BUCKET_NAME : bucketName;
        this.targetPath = StrUtil.isEmpty(targetPath) ? Constants.TARGET_PATH : targetPath;
        this.debug = debug;
    }

    public static UploadFileService create(String ossType, String bucketName, String targetPath, boolean debug) {
        return new UploadFileService(ossType, bucketName, targetPath, debug);
    }

    public Object upload(TaskListener taskListener, FilePath workspace) {
        try {
            LogUtil.taskListener = taskListener;
            LogUtil.println("==============================jenkins plugin----upload file begin============================");

            this.iterateWorkspace(workspace.getRemote());

            LogUtil.println("===============================jenkins plugin----upload file end============================");
        } catch (Exception e) {
            LogUtil.println("上传文件出现异常:" + e.getMessage());
        }
        return null;
    }

    private void iterateWorkspace(String workspacePath) {
        String distPath = workspacePath + File.separator + this.targetPath;
        LogUtil.println("上传的文件目录来源：" + distPath);
        String env = this.getEvnByWorkspace(workspacePath);
        LogUtil.println("发布至环境：" + env);
        String appName = this.getAppNameByWorkspace(workspacePath);
        LogUtil.println("工程名称：" + appName);

        List<File> files = PathUtil.loopFiles(Paths.get(distPath), null);

        LogUtil.println("上传至OSS【" + ossType + "】的bucket：【" + bucketName + "】");

        for (File file : files) {
            String ossFileName = new StringBuilder(appName)
                    .append(File.separator)
                    .append(env)
                    .append(file.getPath().replaceAll(distPath, ""))
                    .toString();

            if (this.debug) {
                LogUtil.println("上传到OSS的文件名是" + ossFileName + ",本地路径是" + file.getAbsolutePath());
            }

            OssFactory.getOssService(this.ossType).upload(this.bucketName,
                    ossFileName,
                    new File(file.getAbsolutePath()));
        }
    }

    private String getEvnByWorkspace(String workspacePath) {
        String key = "/workspace/";

        String jobName = workspacePath.substring(workspacePath.indexOf(key) + key.length());

        String[] jobArray = jobName.split("_");

        switch (jobArray[0]) {
            case Constants.Environment.DEV:
                return "dev";
            case Constants.Environment.TEST:
                return "test";
            case Constants.Environment.PROD:
                return "stag";
            case Constants.Environment.ONLINE:
                return "prod";
            default:
                throw new IllegalArgumentException("环境名称非法，必须是DEV/TEST/PROD/ONLINE");
        }
    }

    private String getAppNameByWorkspace(String workspacePath) {
        String key = "/workspace/";

        String jobName = workspacePath.substring(workspacePath.indexOf(key) + key.length());

        String[] jobArray = jobName.split("_");

        int length = jobArray.length;
        if (length < 4) {
            throw new IllegalArgumentException("jenkins job名称非法，比如PROD_zty_h5_zt-platform");
        }

        return jobArray[length - 1];
    }
}
