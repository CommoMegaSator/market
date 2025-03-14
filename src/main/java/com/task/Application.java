package com.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.task")
@EnableJpaRepositories(basePackages = "com.task")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}