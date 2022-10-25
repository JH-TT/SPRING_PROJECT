package com.mysite.sbb.Model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Table(name = "answer")
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    // 한 질문에 여러개의 댓글을 달 수 있으니 ManyToOne을 넣는다.
    @ManyToOne
    private Question question;

    @ManyToOne
    private SiteUser author;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<SiteUser> voter;

    public Answer(String content, Question question, SiteUser author) {
        this.content = content;
        this.question = question;
        this.author = author;
    }
}
