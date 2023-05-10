package com.mysite.sbb.DTO;

import com.mysite.sbb.Model.SiteUser;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private Long id;
    private String name;
    private String email;
    private boolean nameCheck;
    private boolean emailCheck;

    public SessionUser(SiteUser siteUser) {
        this.id = siteUser.getId();
        this.name = siteUser.getUsername();
        this.email = siteUser.getEmail();
        this.nameCheck = siteUser.isNameChange();
        this.emailCheck = siteUser.isEmailCheck();
    }
}
