package com.mysite.sbb.Model;

import com.mysite.sbb.Enum.UserRole;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "siteuser")
public class SiteUser extends BaseTimeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siteuser_id")
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username; // 소셜 로그인은 username에 제약받지 않도록 구현(일반 회원가입으로는 만들 수 없는 아이디로 생성해서 구분하기 위함)

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    private boolean isNameChange = false; // 소셜 로그인시 아이디를 바꿨는지 확인

    @Column(nullable = false)
    private boolean emailCheck = false; // 이메일 인증관련

    //========= 생성 메서드 ==========

    public SiteUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        role = UserRole.USER;
        isNameChange = true;
    }

    @Builder
    public SiteUser(Long id, String username, String password, String email, boolean isNameChange, boolean emailCheck, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isNameChange = isNameChange;
        this.emailCheck = emailCheck;
        this.role = role;
    }

    @Builder
    public SiteUser(String username, String password, String email, boolean isNameChange, boolean emailCheck, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isNameChange = isNameChange;
        this.emailCheck = emailCheck;
        this.role = role;
    }

    // 이름 수정
    public void updateUserName(String username) {
        this.username = username;
        this.isNameChange = true;
    }

    // 권한 가져오기
    public String getRoleKey() {
        return this.role.getValue();
    }

    // 회원정보에서 이름을 변경할 경우
    public void updateUserNameInSetting(String username) {
        this.username = username;
    }

    // 이메일 인증 성공
    public void emailVerifiedSuccess() {
        this.emailCheck = true;
    }
}
