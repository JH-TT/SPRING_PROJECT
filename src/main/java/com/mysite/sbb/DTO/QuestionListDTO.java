package com.mysite.sbb.DTO;

import com.mysite.sbb.Model.Question;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
public class QuestionListDTO {

    private Long id;
    private String subject;
    private String author;
    private int countOfAnswerComment;

    public static QuestionListDTO from(Question question) {
        if (question == null) return null;

        return QuestionListDTO.builder()
                .id(question.getId())
                .subject(question.getSubject())
                .author(question.getAuthor().getUsername())
                .countOfAnswerComment(question.getTotalCountOfAnswerAndComment())
                .build();
    }

    public static Page<QuestionListDTO> toDtoList(Page<Question> pg) {
        return pg.map(QuestionListDTO::from);
    }
}
