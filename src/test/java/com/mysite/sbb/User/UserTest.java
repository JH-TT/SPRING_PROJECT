package com.mysite.sbb.User;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    UserService userService;

    @Test
    @DisplayName("유저 만들기 테스트")
    public void createUser() {
        userService.create("test1", "leejongho9803@gmail.com", "1234");
    }
}
