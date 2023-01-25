package com.mysite.sbb.Model;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.OAuth.userinfo.OAuth2UserInfo;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@ToString
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final SiteUserDTO user;
//    private Map<String, Object> attributes;
    private OAuth2UserInfo oAuth2UserInfo;

    // Form 로그인 시 사용
    public PrincipalDetails(SiteUserDTO user) {
        this.user = user;
    }


    // OAuth2 로그인 시 사용
//    public PrincipalDetails(SiteUser user, Map<String, Object> attributes) {
//        this.user = user;
//        this.attributes = attributes;
//    }
    public PrincipalDetails(SiteUserDTO user, OAuth2UserInfo oAuth2UserInfo) {
        this.user = user;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getProviderId();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserInfo.getAttributes();
    }

    /**
     * UserDetails 구현
     * 해당 유저의 권한목록 리턴
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole().toString());
        return collect;
    }

    /**
     * UserDetails 구현
     * 비밀번호를 리턴
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * PK값을 반환해준다
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 계정 만료 여부
     * true : 만료안됨
     * false : 만료됨
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 계정 비밀번호 만료 여부
     * true : 만료 안됨
     * false : 만료됨
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 활성화 여부
     * true : 활성화됨
     * false : 비활성화됨
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
