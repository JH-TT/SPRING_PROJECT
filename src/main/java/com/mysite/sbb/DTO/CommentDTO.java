package com.mysite.sbb.DTO;

import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Comment;
import com.mysite.sbb.Model.SiteUser;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

    Integer id;
    String content;
    LocalDateTime createDate;
    LocalDateTime modifiedDate;
    Answer answer;
    SiteUser author;
    Set<SiteUser> voter;

    public Comment toEntity() {
        return Comment.builder()
                .id(id)
                .content(content)
                .createDate(createDate)
                .modifiedDate(modifiedDate)
                .answer(answer)
                .author(author)
                .build();
    }

    public static CommentDTO from(Comment comment) {
        if(comment == null) return null;

        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .modifiedDate(comment.getModifiedDate())
                .answer(comment.getAnswer())
                .author(comment.getAuthor())
                .build();
    }
}
