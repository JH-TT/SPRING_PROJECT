package com.mysite.sbb;

import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.SiteUser;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateForm {

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    @NotBlank
    private String password2;


    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    public SiteUser toEntity() {
        return SiteUser.builder()
                .username(username)
                .password(password1)
                .email(email)
                .role(UserRole.USER)
                .isNameChange(true)
                .build();
    }
}
