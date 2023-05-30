package com.mysite.sbb.Model;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
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
import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
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
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answerList = new ArrayList<>();

    // 여러개의 질문이 한 명의 사용자에게 작성될 수 있으니 ManyToOne이다.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "siteuser_id")
    private SiteUser author;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<SiteUser> voter = new HashSet<>();

    //==생성 로직==//
    @Builder
    public Question(String content, String subject, List<Answer> answerList, SiteUser author) {
        this.content = content;
        this.subject = subject;
        this.answerList = answerList;
        this.author = author;
    }

    @Builder
    public Question(String subject, String content, SiteUserDTO userDTO) {
        this.subject = subject;
        this.content = content;
        this.author = userDTO.toEntity();
    }

    @Builder
    public Question(String subject, String content, SiteUser user) {
        this.subject = subject;
        this.content = content;
        author = user;
    }

    //==비즈니스 로직==//
    public int getTotalCountOfAnswerAndComment() {
        return answerList
                .stream()
                .mapToInt(Answer::getCommentCount)
                .sum() + answerList.size();
    }

    public List<AnswerDTO> changeToAnswerListDTO() {
        return answerList.stream().map(AnswerDTO::from)
                .collect(Collectors.toList());
    }

    public void addRecommand(SiteUser siteUser) {
        if (voter.contains(siteUser)) {
            throw new IllegalStateException("이미 추천을 하였습니다.");
        }
        voter.add(siteUser);
    }

    public int countOfVoter() {
        return voter.size();
    }

    public void updateQuestion(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public void removeAnswer(Answer answer) {
        answerList.remove(answer);
    }
    public boolean isLiked(SiteUser siteUser) {
        return voter.contains(siteUser);
    }
}
