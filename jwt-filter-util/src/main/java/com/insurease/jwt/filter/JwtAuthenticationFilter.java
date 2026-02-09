package com.insurease.jwt.filter;

import com.insurease.jwt.util.TokenProviderService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // <--- 1. Import Slf4j
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j // <--- 2. Enables 'log.info' automatically
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProviderService tokenProviderService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String jwt = getJwtFromRequest(request);

            // -------------------------------------------------------------
            // This prints clearly in the console whenever a request arrives with a token
            // -------------------------------------------------------------
            if (jwt != null) {
                log.info("\n\n=================================================");
                log.info("📨 [MICROSERVICE RECEIVER] Request Intercepted!");
                log.info("🔑 Token Detected: {}", jwt);
                log.info("=================================================\n");
            }
            // -------------------------------------------------------------

            // If token exists and is valid according to Utility
            if (StringUtils.hasText(jwt) && tokenProviderService.validateToken(jwt)) {
                String subject = tokenProviderService.getSubjectFromToken(jwt);

                // Create a simple Authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        subject, null, Collections.emptyList());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the auth in Spring Security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // Optional: Log success (Debug level so it doesn't clutter unless needed)
                log.debug("✅ Authentication successful for user: {}", subject);
            }
        } catch (Exception ex) {
            // Use 'log.error' instead of 'logger.error'
            log.error("❌ Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}