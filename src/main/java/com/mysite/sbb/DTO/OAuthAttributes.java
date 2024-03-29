package com.mysite.sbb.DTO;

import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.SiteUser;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter // 구글 로그인 이후 가져온 사용자의 이메일, 이름, 프로필 사진 주소를 저장하는 DTO
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey, name, email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String name, String email) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if (registrationId.equals("naver")) {
            return ofNaver("id", attributes);
        } else if (registrationId.equals("github")) {
            return ofGithub("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("login"))
                .email((String) attributes.get("login") + "@github.com")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 최초가입시
    public SiteUser toEntity() {
        return SiteUser.builder()
                .username(name)
                .email(email)
                .isNameChange(false)
                .role(UserRole.SNS)
                .emailCheck(true)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }
}
