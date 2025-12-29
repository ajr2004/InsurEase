package com.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Tells Spring to scan the current package AND the utility package
@SpringBootApplication(scanBasePackages = {
    "com.customerservice",      // Your Service Code
    "com.insurease.jwt"         // The JWT Utility Code
})
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}