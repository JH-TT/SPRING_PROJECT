package com.mysite.sbb.User;

import com.mysite.sbb.Repository.UserRepository;
import com.mysite.sbb.Service.UserService;
import com.mysite.sbb.jwt.TokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    UserService userService;

    @Test
    @DisplayName("유저 만들기 테스트")
    public void createUser() {
        userService.create("test1", "leejongho9803@gmail.com", "1234");
    }
}
