package com.mysite.sbb.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.EmailToken;
import com.mysite.sbb.Repository.EmailTokenRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTokenServiceImpl implements EmailTokenService{

    private final EmailSenderServiceImpl emailSenderService;
    private final EmailTokenRepository emailTokenRepository;

    // 이메일 인증 토큰 생성
    @Override
    public String createEmailToken(String userEmail, String receiverEmail) {
        Assert.notNull(userEmail, "userEmail은 필수입니다.");
        Assert.hasText(receiverEmail, "receiverEmail은 필수입니다.");

        // 이메일 토큰 저장
        EmailToken emailToken = EmailToken.createEmailToken(userEmail);
        emailTokenRepository.save(emailToken);

        // 이메일 전송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject("회원가입 이메일 인증");
        mailMessage.setText("http://localhost:8080/user/confirm-email?token=" + emailToken.getId());
        emailSenderService.sendEmail(mailMessage);

        return emailToken.getId(); // 인증메일 전송 시 토큰 반환
    }

    // 유효한 토큰 가져오기
    @Override
    public EmailToken findByIdAndExpirationDateAfterAndExpired(String emailTokenId) {

        return emailTokenRepository.findByIdAndExpirationDateAfterAndExpired(emailTokenId, LocalDateTime.now(), false)
                .orElseThrow(
                        () -> new DataNotFoundException("인증 토큰이 없습니다!")
                );
    }
}
