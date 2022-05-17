package com.xhtech.tool.jenkins.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;

import java.io.File;

/**
 * minio OSS
 */
public class MinioOssService implements OssService {
    private static final String ENDPOINT = "http://192.168.xx.xx:9000";

    private static final String ACCESS_KEY = "xxx";
    private static final String SECRET_KEY = "xxxxxx";

    public void upload(String bucketName, String fileName, File localAbsolutePathFile) {
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(ENDPOINT)
                            .credentials(ACCESS_KEY, SECRET_KEY)
                            .build();

            //  判断bucket是否已存在
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // 不存在，新建一个bucket
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 上传
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .filename(localAbsolutePathFile.getAbsolutePath())
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.getStackTrace());
        }
    }
}
