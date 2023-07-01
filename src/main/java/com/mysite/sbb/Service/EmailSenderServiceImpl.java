package com.mysite.sbb.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.IllegalWriteException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl{

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(final String msg, final String receiver) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, receiver);
        message.setSubject("회원가입 인증링크입니다");
        message.setText(msg, "utf-8", "html"); // 내용, charset 타임, subtype
        message.setFrom(new InternetAddress("leejongho9803@gmail.com", "이종호")); // 보내는 사람

        try {
            javaMailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalWriteException();
        }
    }
}
