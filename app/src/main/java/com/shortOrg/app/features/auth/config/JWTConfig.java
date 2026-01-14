package com.shortOrg.app.features.auth.config;

import com.shortOrg.app.features.auth.beans.JwtAuthenticationFilter;
import com.shortOrg.app.features.auth.beans.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JWTConfig {
    @Bean
    JwtTokenProvider jwtTokenProvider() {
        String secret = "CHANGE_ME_CHANGE_ME_CHANGE_ME_CHANGE_ME_32bytes+";
        return new JwtTokenProvider(secret, 15 * 60_000L, 14L * 24 * 60 * 60_000L);
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwt, UserDetailsService uds) {
        return new JwtAuthenticationFilter(jwt, uds);
    }
}
