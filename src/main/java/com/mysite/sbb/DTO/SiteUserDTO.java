package com.mysite.sbb.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.SiteUser;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteUserDTO {
    @NotNull
    Long id;
    @NotNull
    String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    String password;
    @NotNull
    String email;
    UserRole role;

    public SiteUser toEntity() {
        return SiteUser.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .isNameChange(true)
                .role(UserRole.USER)
                .build();
    }

    public static SiteUserDTO from(SiteUser siteUser) {

        return SiteUserDTO.builder()
                .id(siteUser.getId())
                .username(siteUser.getUsername())
                .password(siteUser.getPassword())
                .email(siteUser.getEmail())
                .role(siteUser.getRole())
                .build();
    }
}
