package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SiteUserDTO create(String username, String email, String password) {
        SiteUserDTO siteUserDTO = SiteUserDTO.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        return SiteUserDTO.from(userRepository.save(siteUserDTO.toEntity()));
    }

    @Override
    @Transactional(readOnly = true)
    public SiteUserDTO getUser(String username) {
        SiteUser siteuser = userRepository.findByusername(username);
        if(siteuser != null) {
            return SiteUserDTO.from(siteuser);
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
