package com.mysite.sbb.Controller;


import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Service.EmailTokenServiceImpl;
import com.mysite.sbb.Service.UserService;
import com.mysite.sbb.UserCreateForm;
import com.mysite.sbb.UsernameForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final HttpSession httpSession;

    private final EmailTokenServiceImpl emailTokenService;

    // get요청시 회원가입 템플릿 렌더링
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "login/signup_form";
    }

    // post요청시 회원가입
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, RedirectAttributes re) {
        if (bindingResult.hasErrors()) {
            return "login/signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "login/signup_form";
        }

        try {
            userService.create(userCreateForm);
            emailTokenService.createEmailToken(userCreateForm.getEmail(), userCreateForm.getEmail());
            re.addFlashAttribute("email", "해당 이메일에 인증 링크를 보냈습니다. 확인해 주세요!");
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "login/signup_form";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "login/signup_form";
        }

        return "redirect:/user/login";
    }

    // 실제 로그인을 진행하는 Post방식의 메서드는
    // 스프링 시큐리티가 대신 처리
    @GetMapping("/login")
    public String login() {
        return "login/login_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/setNickName")
    public String goSettingNickNamePage(@ModelAttribute("usernameForm") UsernameForm usernameForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute("user");
        boolean isCheck = sessionUser.isNameCheck();
        if(isCheck) {
            return "redirect:/";
        }
        return "login/setnickname";
    }

    @PostMapping("/setNickName")
    public String setNickName(@Validated @ModelAttribute UsernameForm usernameForm,
                              BindingResult bindingResult,
                              HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/setnickname";
        }

        try{
            SiteUser siteUserDTO = userService.getUser(usernameForm.getUsername());
            bindingResult.rejectValue("username", "changeNameFailed", "중복되는 이름입니다.");
            return "login/setnickname";
        } catch (DataNotFoundException e) {
            HttpSession session = request.getSession();
            SessionUser sessionUser = (SessionUser) session.getAttribute("user");
            log.info("email={}", sessionUser.getEmail());
            log.info("username={}", usernameForm.getUsername());
            SiteUser siteUser = userService.updateUserName(usernameForm.getUsername(), sessionUser.getEmail());
            httpSession.setAttribute("user", new SessionUser(siteUser));
        }

        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/confirm-email")
    public String viewConfirmEmail(@Valid @RequestParam String token) {
        try {
            userService.verifyEmail(token);
            return "<script>" +
                    "alert('이메일 인증에 성공했습니다!');" +
                    "location.href='/'" +
                    "</script>";
        } catch (Exception e) {
            return "<script>" +
                    "alert('이메일 인증에 실패했습니다. 다시 시도해 주세요.');" +
                    "location.href='/'" +
                    "</script>";
        }
    }

    @GetMapping("/resendEmail")
    public String goSendEmailPage() {
        return "/login/reSendEmail";
    }

    @ResponseBody
    @PostMapping("/resendEmail")
    public boolean resendEmail(@RequestParam("email") String email) {
        try {
            SiteUserDTO userByEmail = userService.getUserByEmail(email);
            emailTokenService.createEmailToken(userByEmail.getEmail(), userByEmail.getEmail());
            System.out.println("userByEmail = " + userByEmail);
            return true;
        } catch (DataNotFoundException e) {
            return false;
        }
    }


    @PostConstruct
    public void init() {
        userService.create("test", "test@test.com", "1234");
        userService.create("tes1t", "test1@test.com", "1234");
    }
}
