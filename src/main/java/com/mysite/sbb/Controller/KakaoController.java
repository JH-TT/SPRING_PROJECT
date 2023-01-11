package com.mysite.sbb.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/kakao")
public class KakaoController {

//    private KakaoLogin kakao_restapi = new KakaoLogin();

    @GetMapping("/oauth")
    public String kakaoConnect() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + "6c10862420b48075014ef5cbfa1d756c");
        url.append("&redirect_uri=http://localhost:8080/kakao/callback");
        url.append("&response_type=code");

        return "redirect:" + url;
    }

    @RequestMapping(value="/callback", produces = "application/json", method = {RequestMethod.GET, RequestMethod.POST})
    public void kakaoLogin(@RequestParam("code") String code,
                            RedirectAttributes ra,
                            HttpSession session,
                            HttpServletResponse response) throws IOException {
        System.out.println("kakao code = " + code);
    }
}
