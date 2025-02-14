package com.disem.API.configs;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${spring.minio.url}")
    private String url;

    @Value("${spring.minio.access.name}")
    private String accessKey;

    @Value("${spring.minio.access.secret}")
    private String accessSecret;

    @PostConstruct
    public void init() {
        System.out.println("✅ MinIO URL: " + url);
        System.out.println("✅ MinIO Access Key: " + accessKey);
        System.out.println("✅ MinIO Secret Key: " + accessSecret);
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, accessSecret)
                .build();
    }

}
