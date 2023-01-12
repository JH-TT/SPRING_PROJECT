package com.mysite.sbb.Model;

import com.mysite.sbb.Enum.UserRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Setter
    private String password;

    private String email;
    @Enumerated(EnumType.STRING)
    @Setter
    private UserRole role;

    @CreationTimestamp
    private Timestamp createTime;

    private String provider; // 어떤 플랫폼인지
    private String providerId; // oauth2를 이용할 경우 아이디값

    @Builder(builderClassName = "UserDetailRegister", builderMethodName = "userDetailRegister")
    public User(String username, String password, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    @Builder(builderClassName = "OAuth2Registor", builderMethodName = "oauth2Register")
    public User(String username, String password, String email, UserRole role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

}
