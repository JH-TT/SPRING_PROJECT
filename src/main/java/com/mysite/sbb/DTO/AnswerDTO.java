package com.mysite.sbb.DTO;

import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Comment;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
    Long question;
    SiteUserDTO author;
    Set<SiteUserDTO> voter;
    List<CommentDTO> commentList;

    public static AnswerDTO from(Answer answer) {
        if(answer == null) return null;

        return AnswerDTO.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .createDate(answer.getCreateDate())
                .modifiedDate(answer.getModifiedDate())
                .question(answer.getQuestion().getId())
                .author(SiteUserDTO.from(answer.getAuthor()))
                .voter(answer.changeToSiteUserDTOSet())
                .commentList(answer.changeToCommentDTOList())
                .build();
    }

    public Answer toEntity() {
        return Answer.builder()
                .content(content)
                .author(author.toEntity())
                .voter(voter.stream().map(SiteUserDTO::toEntity).collect(Collectors.toSet()))
                .commentList(commentList.stream().map(CommentDTO::toEntity).collect(Collectors.toList()))
                .build();
    }
}
