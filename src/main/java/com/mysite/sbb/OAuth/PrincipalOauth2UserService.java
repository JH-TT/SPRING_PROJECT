package com.mysite.sbb.OAuth;

import com.mysite.sbb.DTO.OAuthAttributes;
import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.OAuth.userinfo.GoogleUserInfo;
import com.mysite.sbb.OAuth.userinfo.NaverUserInfo;
import com.mysite.sbb.OAuth.userinfo.OAuth2UserInfo;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.Optional;
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

        OAuth2UserInfo oAuth2UserInfo = null;
        // 플랫폼 (구글, 네이버, 카카오, 깃허브 등등...)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(provider, userNameAttributeName, oAuth2User.getAttributes());

        // 임시 닉네임 만들기
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        attributes.updateName(username);

        SiteUser user = userRepository.findByemail(oAuth2UserInfo.getEmail()).orElse(attributes.toEntity());
        user.emailVerifiedSuccess(); // 소셜 로그인은 이메일 인증이 필요없음
        httpSession.setAttribute("user", new SessionUser(userRepository.save(user)));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }
}
