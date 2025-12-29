package com.insurease.service;

import com.insurease.client.CustomerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import Slf4j
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j // Adds the 'log' object automatically
public class CustomerCacheService {

    private final CustomerClient customerClient;

    @Cacheable(value = "customers", key = "#customerRegNo")
    public Map<String, Object> getCustomerByRegNo(String customerRegNo) {
        
        // Using logger instead of System.out.println
        log.info("Fetching customer from Feign Client (Cache Miss) for RegNo: {}", customerRegNo);

        return customerClient.getCustomerByRegNo(customerRegNo);
    }
}