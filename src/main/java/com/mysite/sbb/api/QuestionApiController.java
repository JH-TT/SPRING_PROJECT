package com.mysite.sbb.api;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.QuestionListDTO;
import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.QuestionForm;
import com.mysite.sbb.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class QuestionApiController {

    private final QuestionService questionService;

    @PostMapping("/api/questions")
    public Long addQuestion(@RequestBody QuestionForm questionForm, String email) {
        return questionService.create(questionForm.getSubject(), questionForm.getContent(), email);
    }

    @GetMapping("/questiongetlist")
    public Page<QuestionListDTO> getQuestionList() {
        return questionService.getListV1(0, "");
    }

    @GetMapping("/question/details/{id}")
    public QuestionDTO getQuestion(@PathVariable("id") Long id) {
        return questionService.getQuestion(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/voteCheck/{id}")
    public boolean QuestionVoteCheck(@PathVariable("id") Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        SessionUser user = (SessionUser) session.getAttribute("user");
        System.out.println("여기 지남");
        return questionService.checkLiked(id, user.getName());
    }
}
