package com.mysite.sbb;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotBlank
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    @NotBlank
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    @NotBlank
    private String password2;


    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;
}
