package com.mysite.sbb.Model;

import com.mysite.sbb.Enum.UserRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    private String picture;
    @Enumerated(EnumType.STRING)
    @Setter
    @Column(nullable = false)
    private UserRole role;

    @CreationTimestamp
    private Timestamp createTime;

    @Builder
    public User(String username, String picture, String email, UserRole role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.picture = picture;
    }
    public User update(String username, String picture) {
        this.username = username;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return this.role.getValue();
    }

}
