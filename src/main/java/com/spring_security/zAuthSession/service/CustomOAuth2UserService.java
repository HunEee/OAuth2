package com.spring_security.zAuthSession.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.spring_security.zAuthSession.dto.CustomOAuth2User;
import com.spring_security.zAuthSession.dto.NaverResponse;
import com.spring_security.zAuthSession.dto.OAuth2Response;
import com.spring_security.zAuthSession.entity.UserEntity;
import com.spring_security.zAuthSession.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
//        else if (registrationId.equals("google")) {
//
//            oAuth2Response = new GoogleReponse(oAuth2User.getAttributes());
//        }
        else {
            return null;
        }
        
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username);
        String role = "ROLE_USER";
        
        // 처음 로그인 시 데이터를 저장 -> 두번째 부터는 데이터만 업데이트
        if (existData == null) { 
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setRole(role);

            userRepository.save(userEntity);
        }
        else {
            existData.setUsername(username);
            existData.setEmail(oAuth2Response.getEmail());

            role = existData.getRole();

            userRepository.save(existData);
        }

        return new CustomOAuth2User(oAuth2Response, role);
    }
	
}
