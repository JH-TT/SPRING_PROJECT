package com.mysite.sbb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RedisApiController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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
}
