package com.mysite.sbb.OAuth;

import com.mysite.sbb.DTO.OAuthAttributes;
import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PrincipalOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 플랫폼 (구글, 네이버, 카카오, 깃허브 등등...)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(provider, userNameAttributeName, oAuth2User.getAttributes());

        // 임시 닉네임 만들기
        String uuid = UUID.randomUUID().toString().substring(0, 10);
        String username = provider + "_" + uuid;
        attributes.updateName(username);

        // attributes -> 로그인 성공후 유저 데이터가 담긴 상태
        SiteUser user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(userRepository.save(user)));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 소셜로그인시 기존 회원이 존재하면 수정날짜 정보만 업데이트하여 기존의 데이터는 그대로 보존
    private SiteUser saveOrUpdate(OAuthAttributes attributes) {
        SiteUser siteUser = userRepository.findByemail(attributes.getEmail()).orElse(attributes.toEntity());
        // SNS 로그인은 이메일 인증 패스
        siteUser.emailVerifiedSuccess();
        return siteUser;
    }
}
