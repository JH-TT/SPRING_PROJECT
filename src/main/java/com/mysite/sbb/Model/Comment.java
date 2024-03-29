package com.mysite.sbb.Model;

import com.mysite.sbb.DTO.SiteUserDTO;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@SQLDelete(sql = "UPDATE comment SET deleted = true WHERE comment_id = ?")
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "siteuser_id")
    private SiteUser author;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<SiteUser> voter = new HashSet<>();

    private boolean deleted = Boolean.FALSE;

    //==생성 메서드==//
    @Builder
    public Comment(String content, SiteUser author) {
        this.content = content;
        this.author = author;
    }
    public static Comment createComment(Answer answer, String content, SiteUser author) {
        Comment comment = new Comment(content, author);
        comment.setAnswer(answer);
        return comment;
    }

    //==연관관계 메서드==//
    public void setAnswer(Answer answer) {
        this.answer = answer;
        answer.getCommentList().add(this);
    }

    public int countOfVoter() {
        return voter.size();
    }

    public void vote(SiteUser siteUser) {
        voter.add(siteUser);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isLiked(SiteUser siteUser) {
        return voter.contains(siteUser);
    }
}
