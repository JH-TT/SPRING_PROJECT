package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.Authority;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import com.mysite.sbb.jwt.TokenProvider;
import com.mysite.sbb.DTO.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Override
    public SiteUserDTO create(String username, String email, String password) {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        SiteUserDTO siteUserDTO = SiteUserDTO.builder()
                .username(username)
//                .password(passwordEncoder.encode(password))
                .password(password)
                .email(email)
                .authorityDtoSet(Collections.singleton(authority))
                .activated(true)
                .build();
        return SiteUserDTO.from(userRepository.save(siteUserDTO.toEntity()));
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
    @Transactional
    public TokenDTO login(String username, String password) {
        // 로그인 아이디, 비번을 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        // 실제 검증 (사용자 비밀번호 체크)이 이루어짐
        // authenticate 메서드가 실행될 때 CustomUserDetailService에서 만든 loadUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDTO tokenDTO = tokenProvider.generateToken(authentication);

        return tokenDTO;
    }
}
