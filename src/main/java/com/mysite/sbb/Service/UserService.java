package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.UserCreateForm;

import java.util.Optional;

public interface UserService {
    SiteUser create(String username, String email, String password);
    SiteUserDTO create(UserCreateForm createForm);
    SiteUserDTO create(SiteUserDTO siteUserDTO);
    SiteUser getUser(String username);
    SiteUserDTO getUserByEmail(String email);
    SiteUser updateUserName(String username, String email);
    void verifyEmail(String token);
}
