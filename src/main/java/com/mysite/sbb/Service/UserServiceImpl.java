package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import com.mysite.sbb.UserCreateForm;
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
    public SiteUser create(String username, String email, String password) {
        SiteUserDTO siteUserDTO = SiteUserDTO.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        return userRepository.save(siteUserDTO.toEntity());
    }

    @Override
    public SiteUserDTO create(UserCreateForm userCreateForm) {
        userCreateForm.setPassword1(passwordEncoder.encode(userCreateForm.getPassword1()));
        return SiteUserDTO.from(userRepository.save(userCreateForm.toEntity()));
    }

    @Override
    public SiteUserDTO create(SiteUserDTO siteUserDTO) {
        return SiteUserDTO.from(userRepository.save(siteUserDTO.toEntity()));
    }

    @Override
    @Transactional(readOnly = true)
    public SiteUser getUser(String username) {
        SiteUser siteUser = userRepository.findByusername(username).orElseThrow(
                () -> new DataNotFoundException("해당 회원이 존재하지 않습니다.")
        );
        return siteUser;
    }

    @Override
    @Transactional(readOnly = true)
    public SiteUserDTO getUserByEmail(String email) {
        SiteUser siteUser = userRepository.findByemail(email).orElseThrow(
                () -> new DataNotFoundException("해당 회원이 존재하지 않습니다.")
        );
        return SiteUserDTO.from(siteUser);
    }

    @Override
    public SiteUser updateUserName(String username, String email) {
        SiteUser siteUser1 = userRepository.findByemail(email).orElseThrow(
                () -> new DataNotFoundException("해당 회원이 존재하지 않습니다"));
        siteUser1.updateUserName(username);
        return siteUser1;
    }
}
