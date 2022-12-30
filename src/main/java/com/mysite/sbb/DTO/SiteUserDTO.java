package com.mysite.sbb.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.Authority;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.UserRepository;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
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
    LocalDateTime createDate;
    boolean activated;
    private Set<AuthorityDTO> authorityDtoSet;

    public SiteUser toEntity() {
        return SiteUser.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .createDate(createDate)
                .activated(activated)
                .authorities(authorityDtoSet.stream()
                        .map(authorityDTO -> Authority.builder().authorityName(authorityDTO.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }

    public static SiteUserDTO from(SiteUser siteUser) {
        if(siteUser == null) return null;

        return SiteUserDTO.builder()
                .id(siteUser.getId())
                .username(siteUser.getUsername())
                .password(siteUser.getPassword())
                .email(siteUser.getEmail())
                .createDate(siteUser.getCreateDate())
                .activated(siteUser.isActivated())
                .authorityDtoSet(siteUser.getAuthorities().stream()
                        .map(authority -> AuthorityDTO.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
