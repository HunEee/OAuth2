package com.spring_security.zAuthSession.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.spring_security.zAuthSession.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }
	
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable()) // 개발 환경에서 꺼줌
        	.formLogin((login) -> login.disable()) // 폼로그인 사용X
        	.httpBasic((basic) -> basic.disable()); // http basic 방식 사용X

        http.oauth2Login(
        		(oauth2) -> oauth2.userInfoEndpoint(
							  (userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOAuth2UserService)
                        	)
		);

        //경로에 대한 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/WEB-INF/jsp/**", "/", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated()
        );

        return http.build();
    }
	
}
