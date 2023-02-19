package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;

import java.util.Optional;

public interface UserService {
    SiteUserDTO create(String username, String email, String password);
    SiteUserDTO create(SiteUserDTO siteUserDTO);
    SiteUserDTO getUser(String username);
    SiteUserDTO getUserByEmail(String email);

    void updateUserName(String username, String email, PrincipalDetails principalDetails);
//    SiteUser updateUserName(String username, String email);
}
