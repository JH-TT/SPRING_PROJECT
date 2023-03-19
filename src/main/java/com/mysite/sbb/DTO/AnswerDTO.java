package com.mysite.sbb.DTO;

import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Comment;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    Long id;
    String content;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    Question question;
    SiteUser author;
    Set<SiteUser> voter;
    List<Comment> commentList;

    public Answer toEntity() {
        return Answer.builder()
                .id(id)
                .content(content)
                .question(question)
                .author(author)
                .voter(voter)
                .commentList(commentList)
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
                .author(answer.getAuthor())
                .voter(answer.getVoter())
                .commentList(answer.getCommentList())
                .build();
    }
}
