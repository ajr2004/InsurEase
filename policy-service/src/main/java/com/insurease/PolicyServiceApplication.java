package com.insurease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // ✅ required import

@SpringBootApplication
@EnableFeignClients(basePackages = "com.insurease.client") // Enables Feign Client scanning
public class PolicyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolicyServiceApplication.class, args);
    }
}
