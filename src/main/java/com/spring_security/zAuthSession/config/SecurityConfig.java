package com.spring_security.zAuthSession.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.spring_security.zAuthSession.oauth2.CustomClientRegistrationRepo;
import com.spring_security.zAuthSession.oauth2.CustomOAuth2AuthorizedClientService;
import com.spring_security.zAuthSession.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    
	private final CustomClientRegistrationRepo customClientRegistrationRepo;
	
    private final CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService;
    private final JdbcTemplate jdbcTemplate;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomClientRegistrationRepo customClientRegistrationRepo, CustomOAuth2AuthorizedClientService customOAuth2AuthorizedClientService, JdbcTemplate jdbcTemplate) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customClientRegistrationRepo = customClientRegistrationRepo;
        this.customOAuth2AuthorizedClientService = customOAuth2AuthorizedClientService;
        this.jdbcTemplate = jdbcTemplate;
    }
	
   
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable()) // 개발 환경에서 꺼줌
        	//.formLogin((login) -> login.disable()) // 폼로그인 사용X
        	.httpBasic((basic) -> basic.disable()); // http basic 방식 사용X

        //경로에 대한 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated()
        );
        
        //커스텀 로그인
        http.oauth2Login(
        		(oauth2) -> oauth2.loginPage("/login")
        						  .clientRegistrationRepository(customClientRegistrationRepo.clientRegistrationRepository())
        						  .authorizedClientService(customOAuth2AuthorizedClientService.oAuth2AuthorizedClientService(jdbcTemplate, customClientRegistrationRepo.clientRegistrationRepository()))
                				  .userInfoEndpoint((userInfoEndpointConfig) ->
                				  						userInfoEndpointConfig.userService(customOAuth2UserService))
		);

        return http.build();
    }
	
}
