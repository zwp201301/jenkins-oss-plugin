package com.xhtech.tool.jenkins.service;

import com.google.common.collect.Maps;
import com.xhtech.tool.jenkins.constant.Constants;

import java.util.Map;

public class OssFactory {
    public static final Map<String, OssService> ossServiceMap = Maps.newConcurrentMap();

    static {
        ossServiceMap.put(Constants.OssType.ALIYUN, new AliyunOssService());
        ossServiceMap.put(Constants.OssType.MINIO, new MinioOssService());
    }

    public static OssService getOssService(String ossType) {
        return ossServiceMap.get(ossType);
    }
}
