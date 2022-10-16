package com.mysite.sbb;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class QuestionForm {

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max=200) // 200바이트를 넘으면 안된다.
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
