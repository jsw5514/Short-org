package com.shortOrg.app.features.auth.config;

import com.shortOrg.app.features.auth.beans.JwtAuthenticationFilter;
import com.shortOrg.app.features.auth.beans.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JWTConfig {
    @Bean
    JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.accessMs}") long accessMs,
            @Value("${jwt.refreshMs}") long refreshMs
    ) {
        return new JwtTokenProvider(secret, accessMs, refreshMs);
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwt, UserDetailsService uds) {
        return new JwtAuthenticationFilter(jwt, uds);
    }
}
