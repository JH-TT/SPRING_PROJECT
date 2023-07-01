package com.mysite.sbb.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.EmailToken;
import com.mysite.sbb.Repository.EmailTokenRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTokenServiceImpl implements EmailTokenService {

    private final EmailSenderServiceImpl emailSenderService;
    private final EmailTokenRepository emailTokenRepository;

    // 이메일 인증 토큰 생성
    @Override
    public String createEmailToken(final String userEmail, final String receiverEmail) throws MessagingException, UnsupportedEncodingException {
        Assert.notNull(userEmail, "userEmail은 필수입니다.");
        Assert.hasText(receiverEmail, "receiverEmail은 필수입니다.");

        // 이메일 토큰 저장
        EmailToken emailToken = EmailToken.createEmailToken(userEmail);
        emailTokenRepository.save(emailToken);

        // 이메일 전송
        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1> 안녕하세요</h1>";
        msg += "<h1> SBB 입니다</h1>";
        msg += "<br>";
        msg += "<p>아래 링크를 클릭해서 회원가입을 완료해주세요<p>";
        msg += "<br>";
        msg += "<p>항상 당신의 꿈을 응원합니다. 감사합니다!<p>";
        msg += "<br>";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'>회원가입 인증 링크입니다.</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "LINK : <a href=" + "http://localhost:8080/user/confirm-email?token=" + emailToken.getId() + ">";
        msg += "이메일 인증하기!</a><div><br/> ";
        msg += "</div>";
        emailSenderService.sendEmail(msg, receiverEmail);

        return emailToken.getId(); // 인증메일 전송 시 토큰 반환
    }

    // 유효한 토큰 가져오기
    @Override
    public EmailToken findByIdAndExpirationDateAfterAndExpired(final String emailTokenId) {

        return emailTokenRepository.findByIdAndExpirationDateAfterAndExpired(emailTokenId, LocalDateTime.now(), false)
                .orElseThrow(
                        () -> new DataNotFoundException("인증 토큰이 없습니다!")
                );
    }
}
