package com.mysite.sbb.Answer;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.QuestionService;
import com.mysite.sbb.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnswerTest {

    @Autowired
    AnswerService answerService;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @Test
    public void create() {
        QuestionDTO questionDTO = questionService.getQuestion(205);
        System.out.println("댓글달기 전");
        System.out.println(questionDTO.toString());;

        SiteUserDTO siteUserDTO = userService.getUser("test123");
        answerService.create(questionDTO, "테스트댓글2", siteUserDTO);

        System.out.println("댓글단 후");
        QuestionDTO questionDTO1 = questionService.getQuestion(205);
        System.out.println(questionDTO1.toString());;
    }
}
