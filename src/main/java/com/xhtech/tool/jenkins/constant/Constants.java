package com.xhtech.tool.jenkins.constant;

public class Constants {
    public static final String DEFAULT_BUCKET_NAME = "html5";

    public static final String TARGET_PATH = "dist";

    public static class OssType {
        public static final String ALIYUN = "aliyun";
        public static final String MINIO = "minio";
    }

    public static class Environment {
        public static final String DEV = "DEV";
        public static final String TEST = "TEST";
        public static final String PROD = "PROD";
        public static final String ONLINE = "ONLINE";
    }
}
