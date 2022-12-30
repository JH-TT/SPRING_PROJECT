package com.mysite.sbb.testService;

import com.mysite.sbb.testDto.UserDTO;
import com.mysite.sbb.Model.Authority;
import com.mysite.sbb.testEntity.TestUser;
import com.mysite.sbb.testException.DuplicateMemberException;
import com.mysite.sbb.testException.NotFoundMemberException;
import com.mysite.sbb.testRepository.TestUserRepository;
import com.mysite.sbb.testUtil.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class TestUserService {
    private final TestUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO signup(UserDTO userDTO) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDTO.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        TestUser user = TestUser.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .nickname(userDTO.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();
        return UserDTO.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDTO getUserWithAuthorities(String username) {
        return UserDTO.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDTO getMyUserWithAuthorities() {
        return UserDTO.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}
