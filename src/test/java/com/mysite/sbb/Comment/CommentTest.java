package com.mysite.sbb.Comment;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.CommentDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.CommentService;
import com.mysite.sbb.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentTest {

    @Autowired
    AnswerService answerService;

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @Test
    public void create() {
        AnswerDTO answerDTO = answerService.getAnswer(14);
        SiteUserDTO siteUserDTO = userService.getUser("test123");
        commentService.create(answerDTO, "테스트 대댓글2", siteUserDTO);
    }

    @Test
    public void modity(){
        CommentDTO commentDTO = commentService.getComment(23);
        commentService.modify(commentDTO, "후후2");
    }
    @Test
    public void delete() {
        CommentDTO commentDTO = commentService.getComment(23);
        commentService.delete(commentDTO);
    }
}
