package com.yuri.shoppingsite.domain.auth;

import com.yuri.shoppingsite.Repository.MemberRepository;
import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.auth.*;
import com.yuri.shoppingsite.domain.user.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo = null;	//추가
        String provider = userRequest.getClientRegistration().getRegistrationId();

        //추가
        if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("kakao")){	//추가
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();	//수정
        String name = provider+"_"+providerId;  			// 사용자가 입력한 적은 없지만 만들어준다

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = encoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다

        String email = oAuth2User.getAttribute("email");
        Role role = Role.USER;

        Member byUsername = memberRepository.findByName(name);

        //DB에 없는 사용자라면 회원가입처리
        if(byUsername == null){
            byUsername = Member.oauth2Register()
                    .name(name).password(password).email(email).role(role)
                    .provider(provider).providerId(providerId)
                    .build();
            memberRepository.save(byUsername);
        }

        return new PrincipalDetails(byUsername, oAuth2UserInfo);
    }
}