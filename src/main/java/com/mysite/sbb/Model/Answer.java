package com.mysite.sbb.Model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    private SiteUser author;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<SiteUser> voter;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Comment> commentList;
}
