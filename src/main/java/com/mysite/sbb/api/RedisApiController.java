package com.mysite.sbb.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
@RequiredArgsConstructor
public class RedisApiController {

    private final RedisTemplate<String, String> redisTemplate;
    private final HttpSession httpSession;

    // csrf.disable()을 해주고 실행해야 한다
    @PostMapping(value = "/redisTest")
    public ResponseEntity<?> addRedisKey() {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set("red", "apple");
        vop.set("green", "watermelon");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/redisTest/{key}")
    public ResponseEntity<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String val = vop.get(key);
        System.out.println("val = " + val);
        return new ResponseEntity<>(val, HttpStatus.OK);
    }

    @GetMapping("/getSessionId")
    public String getSessionId() {
        return httpSession.getId();
    }

    @PostMapping("/setSessionId")
    public String setSessionId() {
        httpSession.setAttribute("black", "hello");
        return httpSession.getId();
    }
}
