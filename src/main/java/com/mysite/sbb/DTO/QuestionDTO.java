package com.mysite.sbb.DTO;


import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    Integer id;
    String content;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    String subject;
    List<Answer> answerList;
    SiteUser author;
    Set<SiteUser> voter;

    // DTO -> Entity
    public Question toEntity() {
        return Question.builder()
                .id(id)
                .content(content)
                .createDate(createDate)
                .modifiedDate(modifiedDate)
                .subject(subject)
                .author(author)
                .answerList(answerList)
                .voter(voter)
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
                .answerList(question.getAnswerList())
                .voter(question.getVoter())
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

    public int answer_cnt() {
        int total = 0;
        for(Answer a : answerList) {
            total += a.getCommentList().size();
        }
        return total;
    }
}
