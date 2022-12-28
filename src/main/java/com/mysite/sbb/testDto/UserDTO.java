package com.mysite.sbb.testDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysite.sbb.testEntity.TestUser;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    private Set<AuthorityDTO> authorityDtoSet;

    public static UserDTO from(TestUser user) {
        if (user == null) return null;

        return UserDTO.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(testAuthority -> AuthorityDTO.builder().authorityName(testAuthority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
