package com.mysite.sbb.DTO;


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
public class QuestionDTO {
    Integer id;
    String content;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    String subject;

    public QuestionDTO(Integer id, String content, LocalDateTime createDate, LocalDateTime modifiedDate, String subject) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.subject = subject;
    }

    // DTO -> Entity
    public Question toEntity() {
        return Question.builder()
                .id(id)
                .content(content)
                .createDate(createDate)
                .modifiedDate(modifiedDate)
                .subject(subject)
                .build();
    }

    // Entity -> DTO
    public static QuestionDTO from(Question question) {
        if(question == null) return null;

        return QuestionDTO.builder()
                .id(question.getId())
                .content(question.getContent())
                .createDate(question.getCreateDate())
                .modifiedDate(question.getModifiedDate())
                .subject(question.getSubject())
                .build();
    }
}
