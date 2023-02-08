package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public SiteUserDTO create(String username, String email, String password) {
        SiteUserDTO siteUserDTO = SiteUserDTO.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .isNameChange(true) // 직접 아이디를 만든 유저는 바로 true로 넘긴다.
                .build();
        return SiteUserDTO.from(userRepository.save(siteUserDTO.toEntity()));
//        SiteUser user = SiteUser.oauth2Register()
//                .username(username).password(password).email(email).role(UserRole.USER)
//                .provider(null).providerId(null)
//                .isNameChange(true)
//                .build();
//        return SiteUserDTO.from(userRepository.save(user));
    }

    @Override
    public SiteUserDTO create(SiteUserDTO siteUserDTO) {
        SiteUser user = SiteUser.builder()
                .username(siteUserDTO.getUsername())
                .password(siteUserDTO.getPassword())
                .email(siteUserDTO.getEmail())
                .role(siteUserDTO.getRole())
                .provider(siteUserDTO.getProvider())
                .providerId(siteUserDTO.getProviderId())
                .isNameChange(siteUserDTO.isNameChange())
                .build();
        return SiteUserDTO.from(userRepository.save(user));
    }
    public SiteUserDTO create2(String username, String email, String password) {
        SiteUser user = SiteUser.oauth2Register()
                .username(username).password(password).email(email).role(UserRole.USER)
                .provider(null).providerId(null)
                .isNameChange(true)
                .build();
        return SiteUserDTO.from(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public SiteUserDTO getUser(String username) {
        Optional<SiteUser> siteuser = userRepository.findByusername(username);
        if(siteuser.isPresent()) {
            return SiteUserDTO.from(siteuser.get());
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    @Override
    public Optional<SiteUser> getUserByEmail(String email) {
        return userRepository.findByemail(email);
    }

    @Override
    @Transactional
    public void updateUserName(String username, String email) {
        Optional<SiteUser> siteUser = userRepository.findByemail(email);
        if (siteUser.isPresent()) {
            SiteUserDTO siteUserDTO = SiteUserDTO.from(siteUser.get());
            siteUserDTO.setUsername(username);
            siteUserDTO.setNameChange(true);
            userRepository.save(siteUserDTO.toEntity());
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
