package com.mysite.sbb.Controller;

import com.mysite.sbb.CommentForm;
import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.CommentService;
import com.mysite.sbb.Service.CommentServiceImpl;
import com.mysite.sbb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final AnswerService answerService;
    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createComment(Model model, @PathVariable("id") Integer id
                                , @RequestParam String content, @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        AnswerDTO answerDTO = this.answerService.getAnswer(id);
        SiteUserDTO siteUserDTO = userService.getUser(principal.getName());
        // 바인딩 오류 추가예정
        commentService.create(answerDTO, content, siteUserDTO);
        return String.format("redirect:/question/detail/%s#answer_%s", answerDTO.getQuestion().getId(), answerDTO.getId());
    }
}
