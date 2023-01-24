package com.mysite.sbb.Controller;


import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Service.UserService;
import com.mysite.sbb.UserCreateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // get요청시 회원가입 템플릿 렌더링
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    // post요청시 회원가입
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            //
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    // 실제 로그인을 진행하는 Post방식의 메서드는
    // 스프링 시큐리티가 대신 처리
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/setNickName")
    public String goSettingNickNamePage() {
        return "setnickname";
    }

    @PostMapping("/setNickName")
    public String setNickName(@Validated @ModelAttribute("username") String username,
                              @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "setnickname";
        }
        String email = (String) oAuth2UserPrincipal.getAttributes().get("email");
        log.info("email={}", email);
        userService.updateUserName(username, email);

        return "redirect:/";
    }

    @GetMapping("/form/loginInfo")
    @ResponseBody
    public String formLoginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        SiteUser siteUser = principal.getUser();
        System.out.println("siteUser = " + siteUser);

        SiteUser siteUser1 = principalDetails.getUser();
        System.out.println("siteUser1 = " + siteUser1);

        return siteUser.toString();
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("attributes = " + attributes);

        Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();

        return attributes.toString(); // 세션에 담기 user 가져올 수 있다.
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
