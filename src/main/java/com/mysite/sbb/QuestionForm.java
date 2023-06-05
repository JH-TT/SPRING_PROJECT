package com.mysite.sbb;

import com.mysite.sbb.Model.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionForm {

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max=200) // 200바이트를 넘으면 안된다.
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    public Question toEntity() {
        return Question.builder()
                .subject(subject)
                .content(content)
                .build();
    }
}
