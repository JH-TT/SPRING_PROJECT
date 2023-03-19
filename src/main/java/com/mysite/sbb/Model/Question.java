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
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Table(name = "question")
@AllArgsConstructor
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String subject;

    // Question에선 여러개의 답글이 생길 수 있으니 One to Many로 지정한다.
    // CascadeType.REMOVE -> 질문이 삭제되면 달린 답변들도 전부 삭제하기 위함.
    // FetchType.LAZY -> 필요시점에 데이터를 가져오기 위함.
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answerList;

    // 여러개의 질문이 한 명의 사용자에게 작성될 수 있으니 ManyToOne이다.
    @ManyToOne
    private SiteUser author;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<SiteUser> voter;
}
