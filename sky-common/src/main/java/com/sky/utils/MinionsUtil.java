package com.sky.utils;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import io.minio.MinioClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Data
@AllArgsConstructor
@Slf4j
public class MinionsUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String upload(byte[] bytes, String objectName) {
        MinioClient minioClient = null;
        String resultUrl = "";

        try {
            // 创建 MinIO 客户端
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKeyId, accessKeySecret)
                    .build();

            // 确保存储桶存在
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                log.info("Bucket created: {}", bucketName);
            }

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("application/octet-stream") // 根据实际内容类型修改
                            .build()
            );

            // 生成文件的访问 URL
            resultUrl = endpoint + "/" + bucketName + "/" + objectName;
            log.info("File uploaded successfully: {}", resultUrl);
        } catch (MinioException e) {
            log.error("MinIO upload failed: ", e);
        } catch (IOException e) {
            log.error("IOException occurred: ", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭资源（MinioClient 不需要显式关闭）
        }
        return resultUrl;
    }
}
