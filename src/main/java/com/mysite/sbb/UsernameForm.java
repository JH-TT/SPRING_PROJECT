package com.mysite.sbb;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UsernameForm {

    @Size(min = 2, max = 10)
    @NotBlank
    @Pattern(regexp = "^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,10}$", message = "한글, 영문, 숫자만 가능하며 2-10자리 가능합니다")
    private String username;
}
