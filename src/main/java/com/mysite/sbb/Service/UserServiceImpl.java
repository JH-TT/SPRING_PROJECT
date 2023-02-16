package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SiteUserDTO create(String username, String email, String password) {
        SiteUserDTO siteUserDTO = SiteUserDTO.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .isNameChange(true) // 직접 아이디를 만든 유저는 바로 true로 넘긴다.
                .provider("EMAIL")
                .build();
        return SiteUserDTO.from(userRepository.save(siteUserDTO.toEntity()));
    }

    @Override
    public SiteUserDTO create(SiteUserDTO siteUserDTO) {
//        SiteUser user = SiteUser.builder()
//                .username(siteUserDTO.getUsername())
//                .password(siteUserDTO.getPassword())
//                .email(siteUserDTO.getEmail())
//                .role(siteUserDTO.getRole())
//                .provider(siteUserDTO.getProvider())
//                .providerId(siteUserDTO.getProviderId())
//                .isNameChange(siteUserDTO.isNameChange())
//                .build();
        return SiteUserDTO.from(userRepository.save(siteUserDTO.toEntity()));
    }

    @Override
    @Transactional(readOnly = true)
    public SiteUserDTO getUser(String username) {
        SiteUser siteUser = userRepository.findByusername(username).orElseThrow(
                () -> new DataNotFoundException("해당 회원이 존재하지 않습니다.")
        );
        return SiteUserDTO.from(siteUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SiteUser> getUserByEmail(String email) {
        return userRepository.findByemail(email);
    }

    @Override
    public void updateUserName(String username, String email, PrincipalDetails principalDetails) {
        SiteUser siteUser1 = userRepository.findByemail(email).orElseThrow(
                () -> new DataNotFoundException("해당 회원이 존재하지 않습니다"));
        siteUser1.updateUserName(username);
    }
}
