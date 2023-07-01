package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    // 로그인을 하면 해당 로직을 거친다.
    @Override
    public UserDetails loadUserByUsername(final String email) throws AuthenticationException {
        SiteUser user = userRepository.findByemail(email).orElseThrow(
                () -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다.\n찾으려는 유저의 이메일 : " + email)
        );

        httpSession.setAttribute("user", new SessionUser(user));

        return new PrincipalDetails(user);
    }
}
