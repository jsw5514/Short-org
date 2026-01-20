package com.shortOrg.app.features.auth.config;

import com.shortOrg.app.features.auth.beans.JwtAuthenticationFilter;
import com.shortOrg.app.features.auth.beans.JwtManager;
import com.shortOrg.app.features.auth.beans.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JWTConfig {
    @Bean
    JwtManager jwtManager(
            @Value("${jwt.accessMs}") long accessMs,
            @Value("${jwt.refreshMs}") long refreshMs,
            JwtTokenProvider jwtTokenProvider) {
        return new JwtManager(jwtTokenProvider, accessMs, refreshMs);
    }
    
    @Bean
    JwtTokenProvider jwtTokenProvider(@Value("${jwt.secret}") String secret){
        return new JwtTokenProvider(secret);
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(JwtManager jwt, UserDetailsService uds) {
        return new JwtAuthenticationFilter(jwt, uds);
    }
}
