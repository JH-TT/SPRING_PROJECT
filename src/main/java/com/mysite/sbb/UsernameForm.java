package com.mysite.sbb;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UsernameForm {

    @NotBlank(message = "유저이름을 입력해 주세요")
    private String username;
}
