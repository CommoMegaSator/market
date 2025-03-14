package com.task.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${storage.url:http://localhost:9000}")
    String storageUrl;

    @Value("${storage.username:minioadmin}")
    String storageUsername;

    @Value("${storage.password:minioadmin}")
    String storagePassword;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(storageUrl)
                .credentials(storageUsername, storagePassword)
                .build();
    }
}