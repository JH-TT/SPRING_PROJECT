package com.mysite.sbb.DTO;

import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class AnswerDTO {
    Integer id;
    String content;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    Question question;

    public AnswerDTO(Integer id, String content, LocalDateTime createDate, LocalDateTime modifiedDate, Question question) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.question = question;
    }

    public Answer toEntity() {
        return Answer.builder()
                .id(id)
                .content(content)
                .createDate(createDate)
                .modifiedDate(modifiedDate)
                .question(question)
                .build();
    }

    public static AnswerDTO from(Answer answer) {
        if(answer == null) return null;

        return AnswerDTO.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .createDate(answer.getCreateDate())
                .modifiedDate(answer.getModifiedDate())
                .question(answer.getQuestion())
                .build();
    }
}
