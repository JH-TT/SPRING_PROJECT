package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.EmailToken;
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

    private final EmailTokenService emailTokenService;

    @Override
    public SiteUser create(final String username, final String email, final String password) {
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
    public SiteUser getUser(final String username) {
        SiteUser siteUser = userRepository.findByusername(username).orElseThrow(
                () -> new DataNotFoundException("해당 회원이 존재하지 않습니다.")
        );
        return siteUser;
    }

    @Override
    @Transactional(readOnly = true)
    public SiteUserDTO getUserByEmail(final String email) {
        SiteUser siteUser = userRepository.findByemail(email).orElseThrow(
                () -> new DataNotFoundException("해당 회원이 존재하지 않습니다.")
        );
        return SiteUserDTO.from(siteUser);
    }

    @Override
    public SiteUser updateUserName(final String username, final String email) {
        SiteUser siteUser1 = userRepository.findByemail(email).orElseThrow(
                () -> new DataNotFoundException("해당 회원이 존재하지 않습니다"));
        siteUser1.updateUserName(username);
        return siteUser1;
    }

    @Override
    @Transactional
    public void verifyEmail(final String token) {
        // 이메일 토큰을 가져온다.
        EmailToken findEmailToken = emailTokenService.findByIdAndExpirationDateAfterAndExpired(token);

        // 이메일 성공 인증 로직
        // 성공하면 유저의 emailcheck를 true로 변경한다.
        SiteUser siteUser = userRepository.findByemail(findEmailToken.getUserEmail()).orElseThrow(
                () -> new DataNotFoundException("유저가 존재하지 않습니다.")
        );
        findEmailToken.setTokenToUsed(); // 토큰 사용완료
        siteUser.emailVerifiedSuccess();
    }


}
