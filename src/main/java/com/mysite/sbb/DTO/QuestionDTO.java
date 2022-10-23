package com.mysite.sbb.DTO;


import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    SiteUser author;

    public QuestionDTO(Integer id, String content, LocalDateTime createDate, LocalDateTime modifiedDate, String subject, SiteUser author) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.subject = subject;
        this.author = author;
    }

    // DTO -> Entity
    public Question toEntity() {
        return Question.builder()
                .id(id)
                .content(content)
                .createDate(createDate)
                .modifiedDate(modifiedDate)
                .subject(subject)
                .author(author)
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
                .author(question.getAuthor())
                .build();
    }
}
