package com.mysite.sbb.Question;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Service.QuestionService;
import com.mysite.sbb.Service.UserService;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.AttributeAccessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionTest {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;
    @Test
    public void create() {
        SiteUserDTO siteUserDTO = userService.getUser("test123");
        questionService.create("테스트입니다.", "하이요", siteUserDTO);
    }
}
