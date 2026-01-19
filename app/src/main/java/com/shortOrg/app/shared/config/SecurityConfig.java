package com.shortOrg.app.shared.config;
import com.shortOrg.app.features.auth.beans.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtFilter
    ) throws Exception {

        return http
                .csrf(csrf -> csrf.disable()) //csrf 비활성화
                .cors(cors -> cors.disable()) //cors 비활성화
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 비활성화
                .formLogin(f -> f.disable()) //기본 폼 로그인 비활성화
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    
                })
                .authorizeHttpRequests(auth -> auth  //url별 허용/거부 정책 설정
                        .requestMatchers(
                                "/api/auth/login", //로그인
                                "/api/users", //회원가입
                                "/api/users/exists" //id 중복 확인
                                ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/posts/category/*", //게시글 목록 확인
                                "/api/posts/id/*" //게시글 단건 조회
                                ).permitAll()
                        .requestMatchers("/api/auth/refresh").hasRole("REFRESH")
                        .anyRequest().hasRole("ACCESS")
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) //jwt 인증을 처리하는 필터 추가
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }
}

