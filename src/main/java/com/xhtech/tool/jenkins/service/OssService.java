package com.xhtech.tool.jenkins.service;

import java.io.File;

public interface OssService {
    void upload(String bucketName, String fileName, File localAbsolutePathFile);
}
