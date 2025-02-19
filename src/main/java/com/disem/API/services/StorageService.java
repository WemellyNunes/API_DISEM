package com.disem.API.services;

import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class StorageService {

    @Autowired
    private MinioClient minioClient;

    private static final String BUCKET_NAME = "sinfra";

    public void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar o upload: " + e.getMessage());
        }
    }

    public byte[] downloadFile(String fileUrl) {
        try {
            String objectName = fileUrl.substring(fileUrl.indexOf(BUCKET_NAME) + BUCKET_NAME.length() + 1); // Extrai o caminho correto

            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .build()
            );

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            return buffer.toByteArray();

        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            System.err.println("Erro ao baixar arquivo: " + e.getMessage());
            return null;
        }
    }
}
