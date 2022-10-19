package com.mysite.sbb.Service;

import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public SiteUser create(String username, String email, String password) {
        String password2 = passwordEncoder.encode(password);
        SiteUser siteUser = new SiteUser(username, password2, email);
        userRepository.save(siteUser);
        return siteUser;
    }
}
