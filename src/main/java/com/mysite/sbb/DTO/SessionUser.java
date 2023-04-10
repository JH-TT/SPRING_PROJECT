package com.mysite.sbb.DTO;

import com.mysite.sbb.Model.SiteUser;

import java.io.Serializable;

public class SessionUser implements Serializable {

    private Long id;
    private String name;
    private String email;
    private boolean nameCheck;

    public SessionUser(SiteUser siteUser) {
        this.id = siteUser.getId();
        this.name = siteUser.getUsername();
        this.email = siteUser.getEmail();
        this.nameCheck = siteUser.isNameChange();
    }
}
