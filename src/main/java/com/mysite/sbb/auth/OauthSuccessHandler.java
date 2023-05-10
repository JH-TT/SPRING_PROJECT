package com.mysite.sbb.auth;

import com.mysite.sbb.DTO.SessionUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OauthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공후 session에 저장된 이름 가져옴
        HttpSession session = request.getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute("user");

        // 이름 중복체크
        if (sessionUser.isNameCheck()) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/user/setNickName");
        }
    }
}
