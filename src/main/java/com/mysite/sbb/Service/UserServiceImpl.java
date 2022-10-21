package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public SiteUserDTO create(String username, String email, String password) {
        String password2 = passwordEncoder.encode(password);
        SiteUser siteUser = new SiteUser(username, password2, email);
        userRepository.save(siteUser);
        return SiteUserDTO.from(siteUser);
    }

    @Override
    public SiteUserDTO getUser(String username) {
        Optional<SiteUser> siteuser = userRepository.findByusername(username);
        if(siteuser.isPresent()) {
            return SiteUserDTO.from(siteuser.get());
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
