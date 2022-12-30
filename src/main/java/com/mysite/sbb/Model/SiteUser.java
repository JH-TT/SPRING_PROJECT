package com.mysite.sbb.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.sbb.Enum.UserRole;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Table(name = "siteuser")
@Builder
public class SiteUser {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String email;

    @CreatedDate
    private LocalDateTime createDate;
    @JsonIgnore
    private boolean activated;

    // ManyToMany, JoinTable은 User객체와 권한객체의 다대다 관계를
    // 일대다, 다대일 관계의 조인 테이블로 정의했다는 뜻이다.
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities;
}
