package com.insurease.jwt.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenProviderService {

    // Injected from application.properties of the service using this utility
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:3600000}") // Default 1 hour if not set
    private long jwtExpirationMs;

    private SecretKey key;
    private Cache<String, String> tokenCache;

    @PostConstruct
    public void init() {
        // Initialize key from secret
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        // Initialize Caffeine Cache
        // Cache expires slightly before the actual token expires to be safe
        long cacheExpiry = Math.max(jwtExpirationMs - 60000, jwtExpirationMs / 2);

        this.tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(cacheExpiry, TimeUnit.MILLISECONDS)
                .maximumSize(100) // Max 100 active service tokens held
                .recordStats()
                .build();

        log.info("JWT Provider initialized with cache expiry: {}ms", cacheExpiry);
    }

    /**
     * CORE LOGIC: Generates token only if not in cache.
     * Used by Sender Service (Policy Service).
     */
    public String getValidToken(String serviceName) {
        // Try to get from cache first
        String cachedToken = tokenCache.getIfPresent(serviceName);

        if (cachedToken != null) {
            log.debug("Returning CACHED token for service: {}", serviceName);
            return cachedToken;
        }

        // If not found, generate new one
        log.info("Generating NEW token for service: {}", serviceName);
        String newToken = generateToken(serviceName);

        // Put in cache
        tokenCache.put(serviceName, newToken);

        return newToken;
    }

    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Used by Receiver Service (Customer Service) to validate incoming tokens.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            log.error("JWT Validation failed: {}", e.getMessage());
        }
        return false;
    }

    public String getSubjectFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}