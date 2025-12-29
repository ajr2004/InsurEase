 package com.insurease.client;

import com.insurease.jwt.util.TokenProviderService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    private final TokenProviderService tokenProviderService;

    @Override
    public void apply(RequestTemplate template) {
        // 1. Get a valid token (cached or new) from our Utility
        String token = tokenProviderService.getValidToken("policy-service-client");
        
        // 2. Add it to the Authorization header
        template.header("Authorization", "Bearer " + token);
        
        log.info("SECRET TOKEN: {}", token);
    }
}