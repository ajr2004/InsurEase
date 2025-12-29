package com.insurease.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "customer-service", url = "http://localhost:8081")
public interface CustomerClient {

    // Fetch customer details using registration number
    @GetMapping("/api/customers/regno/{customerRegNo}")
    Map<String, Object> getCustomerByRegNo(@PathVariable("customerRegNo") String customerRegNo);
}
