package com.mysite.sbb.DTO;


import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    Long id;
    String content;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    String subject;
    List<AnswerDTO> answerList;
    SiteUserDTO author;
    Set<SiteUserDTO> voter;
    int countOfAnswerComment;

    // DTO -> Entity
    public Question toEntity() {
        return Question.builder()
                .id(id)
                .content(content)
                .subject(subject)
                .author(author.toEntity())
                .answerList(answerList.stream().map(AnswerDTO::toEntity).collect(Collectors.toList()))
                .voter(voter.stream().map(SiteUserDTO::toEntity).collect(Collectors.toSet()))
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
                .author(SiteUserDTO.from(question.getAuthor()))
                .answerList(question.changeToAnswerListDTO())
                .voter(question.getVoter().stream().map(SiteUserDTO::from).collect(Collectors.toSet()))
                .countOfAnswerComment(question.getTotalCountOfAnswerAndComment())
                .build();
    }

    public static Page<QuestionDTO> toDtoList(Page<Question> pg) {
        return pg.map(QuestionDTO::from);
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                ", subject='" + subject + '\'' +
                ", answerList=" + answerList +
                ", author=" + author +
                ", voter=" + voter +
                '}';
    }
}
