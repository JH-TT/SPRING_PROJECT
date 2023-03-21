package com.mysite.sbb.Model;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Enum.UserRole;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Table(name = "siteuser")
@Builder
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siteuser_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @CreatedDate
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String provider; // 플랫폼 어디인지
    private String providerId; // oauth2를 이용할 경우 아이디값
    private boolean isNameChange; // 소셜 로그인시 아이디를 바꿨는지 확인

    public SiteUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        role = UserRole.USER;
        provider = "EMAIL";
        isNameChange = true;
    }

    @Builder(builderClassName = "UserDetailRegister", builderMethodName = "userDetailRegister")
    public SiteUser(String username, String password, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Builder(builderClassName = "OAuth2Registor", builderMethodName = "oauth2Register")
    public SiteUser(String username, String password, String email, UserRole role, String provider, String providerId, boolean isNameChange) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.isNameChange = isNameChange;
    }

    public void updateUserName(String username) {
        this.username = username;
        this.isNameChange = true;
    }
}
