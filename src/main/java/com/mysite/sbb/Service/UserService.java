package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DTO.TokenDTO;

public interface UserService {
    SiteUserDTO create(String username, String email, String password);
    SiteUserDTO getUser(String username);
    TokenDTO login(String username, String password);
}
