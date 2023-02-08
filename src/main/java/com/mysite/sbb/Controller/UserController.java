package com.mysite.sbb.Controller;


import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Service.UserService;
import com.mysite.sbb.UserCreateForm;
import com.mysite.sbb.UsernameForm;
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

import javax.validation.Valid;
import java.security.Principal;
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
        return "login/signup_form";
    }

    // post요청시 회원가입
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login/signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "login/signup_form";
        }
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "login/signup_form";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "login/signup_form";
        }

        return "redirect:/";
    }

    // 실제 로그인을 진행하는 Post방식의 메서드는
    // 스프링 시큐리티가 대신 처리
    @GetMapping("/login")
    public String login() {
        return "login/login_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/setNickName")
    public String goSettingNickNamePage(@ModelAttribute("usernameForm") UsernameForm usernameForm ,Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("goSettingNickNamePage.name = {}", authentication.getName());
        SiteUserDTO siteUser = principal.getUser();
        log.info("siteUser={}", siteUser.toString());
        boolean isCheck = siteUser.isNameChange();
        if(isCheck) {
            return "redirect:/";
        }
        return "login/setnickname";
    }

    @PostMapping("/setNickName")
    public String setNickName(@Validated @ModelAttribute UsernameForm usernameForm,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (bindingResult.hasErrors()) {
            return "login/setnickname";
        }

        try{
            SiteUserDTO siteUserDTO = userService.getUser(usernameForm.getUsername());
            bindingResult.rejectValue("username", "changeNameFailed", "중복되는 이름입니다.");
            return "login/setnickname";
        } catch (DataNotFoundException e) {
            String email = (String) principalDetails.getAttributes().get("email");
            log.info("email={}", email);
            log.info("username={}", usernameForm.getUsername());
            userService.updateUserName(usernameForm.getUsername(), email);
        }

        return "redirect:/";
    }

    @GetMapping("/form/loginInfo")
    @ResponseBody
    public String formLoginInfo(Principal principal) {
//        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        SiteUserDTO siteUser = userService.getUser(principal.getName());
        log.info("siteUser = " + siteUser.toString());

        return siteUser.toString();
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes = " + attributes);

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
        log.info("result = {}", result);
        return result;
    }
}
