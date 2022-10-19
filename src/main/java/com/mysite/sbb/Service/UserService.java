package com.mysite.sbb.Service;

import com.mysite.sbb.Model.SiteUser;

public interface UserService {

    SiteUser create(String username, String email, String password);

}
