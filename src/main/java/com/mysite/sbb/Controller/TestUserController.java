package com.mysite.sbb.Controller;

import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.User;
import com.mysite.sbb.Repository.TestUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestUserController {
    private TestUserRepository testUserRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("loginForm")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute User user    ) {
        user.setRole(UserRole.USER);

        String encodePwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePwd);

        testUserRepository.save(user);
        return "redirect:/loginForm";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
        return "user";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "menager";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    // OAuth로 로그인 시 이 방식을 이용하면 CastException 발생
    @GetMapping("/form/loginInfo")
    @ResponseBody
    public String formLoginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        System.out.println("user = " + user);

        User user1 = principalDetails.getUser();
        System.out.println("user1 = " + user1);

        return user.toString();
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal) {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("attributes = " + attributes);

        Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();

        return attributes.toString(); // 세션에 담긴 user 가져올 수 있음
    }

    @GetMapping("/loginInfo")
    @ResponseBody
    public String loginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String result = "";

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        if (principal.getUser().getProvider() == null) {
            result = result + "Form 로그인 : " + principal;
        } else {
            result = result + "OAuth2 로그인 : " + principal;
        }
        return result;
    }
}
