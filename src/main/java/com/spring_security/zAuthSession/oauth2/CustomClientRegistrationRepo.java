package com.spring_security.zAuthSession.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class CustomClientRegistrationRepo {

    private final SocialClientRegistration socialClientRegistration;

    public CustomClientRegistrationRepo(SocialClientRegistration socialClientRegistration) {
        this.socialClientRegistration = socialClientRegistration;
    }

    //인메모리 DB 에 넣어줌 -> 메모리가 넘칠 일이 없음(많아봤자 10개)
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(socialClientRegistration.naverClientRegistration());
        // return new InMemoryClientRegistrationRepository(socialClientRegistration.naverClientRegistration(), socialClientRegistration.googleClientRegistration());
    }
}
