package com.mysite.sbb.Model;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.CommentDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.*;

@Getter
@Entity
@SQLDelete(sql = "UPDATE answer SET deleted = true WHERE answer_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 한 질문에 여러개의 댓글을 달 수 있으니 ManyToOne을 넣는다.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "siteuser_id")
    private SiteUser author;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<SiteUser> voter = new HashSet<>();

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    private boolean deleted = Boolean.FALSE;

    //==생성 메서드==//
    @Builder
    public Answer(Long id, String content, SiteUser author, List<Comment> commentList) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.commentList = commentList;
    }
    @Builder
    public Answer(String content, SiteUser author, List<Comment> commentList) {
        this.content = content;
        this.author = author;
        this.commentList = commentList;
    }

    @Builder
    public Answer(String content, SiteUser author) {
        this.content = content;
        this.author = author;
    }

    public static Answer createAnswer(String content, Question question, SiteUser author) {
        Answer answer = new Answer(content, author);
        answer.setQuestion(question);
        return answer;
    }

    //==연관관계 메서드==//
    public void setQuestion(Question question) {
        this.question = question;
        question.getAnswerList().add(this);
    }

    public int getCommentCount() {
        return commentList.size();
    }

    public List<CommentDTO> changeToCommentDTOList() {
        return commentList.stream().map(CommentDTO::from)
                .collect(Collectors.toList());
    }

    public void vote(SiteUser siteUser) {
        voter.add(siteUser);
    }

    public int countOfVoter() {
        return voter.size();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public boolean isLiked(SiteUser siteUser) {
        return voter.contains(siteUser);
    }
}
