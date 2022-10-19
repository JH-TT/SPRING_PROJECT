package com.mysite.sbb.DTO;

import com.mysite.sbb.Model.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class SiteUserDTO {
    Long id;
    String username;
    String password;
    String email;
    LocalDateTime createDate;

    public SiteUserDTO(Long id, String username, String password, String email, LocalDateTime createDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createDate = createDate;
    }

    public SiteUser toEntity() {
        return SiteUser.builder()
                .id(id)
                .username(username)
                .password(password)
                .createDate(createDate)
                .build();
    }

    public static SiteUserDTO from(SiteUser siteUser) {
        if(siteUser == null) return null;

        return SiteUserDTO.builder()
                .id(siteUser.getId())
                .username(siteUser.getUsername())
                .password(siteUser.getPassword())
                .createDate(siteUser.getCreateDate())
                .build();
    }
}
