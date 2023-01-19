package com.mysite.sbb.OAuth;

import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Model.User;
import com.mysite.sbb.OAuth.userinfo.NaverUserInfo;
import com.mysite.sbb.OAuth.userinfo.OAuth2UserInfo;
import com.mysite.sbb.Repository.TestUserRepository;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final TestUserRepository testUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttriubuteName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        String providerId = oAuth2User.getAttribute("sub");
//        String username = provider + "_" + providerId;
        String username = oAuth2User.getAttribute("name");

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = passwordEncoder.encode("패스워드" + uuid);

        String email = oAuth2User.getAttribute("email");
        UserRole role = UserRole.USER;

        SiteUser byUsername = userRepository.findByusername(username);

        if (byUsername == null) {
            byUsername = SiteUser.oauth2Register()
                    .username(username).password(password).email(email).role(role)
                    .provider(provider).providerId(providerId)
                    .build();
            userRepository.save(byUsername);
        }

        return new PrincipalDetails(byUsername, oAuth2User.getAttributes());
    }
}
