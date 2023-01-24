package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;

public interface UserService {
    SiteUserDTO create(String username, String email, String password);
    SiteUserDTO getUser(String username);

    void updateUserName(String username, String email);
}
