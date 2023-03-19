package com.mysite.sbb.Model;

import com.mysite.sbb.DTO.AnswerDTO;
import lombok.*;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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
    private int countOfComment;

    @ManyToMany
    Set<SiteUser> voter = new HashSet<>();

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    //==연관관계 메서드==//
    public void setQuestion(Question question) {
        this.question = question;
        question.getAnswerList().add(this);
    }

    public void addComment() {
        countOfComment++;
    }
}
