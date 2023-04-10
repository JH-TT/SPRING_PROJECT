package com.mysite.sbb.Model;

import com.mysite.sbb.Enum.UserRole;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "siteuser")
public class SiteUser extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "siteuser_id")
    private Long id;

    @NotBlank @Length(min = 2, max = 10)
    @Pattern(regexp = "^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,10}$") // 한글, 영문, 숫자만 가능하며 2-10자리 가능
    @Column(unique = true)
    private String username;

    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    private boolean isNameChange; // 소셜 로그인시 아이디를 바꿨는지 확인

    @Column(nullable = false)
    private boolean emailCheck = false; // 이메일 인증관련

    public SiteUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        role = UserRole.USER;
        isNameChange = true;
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
