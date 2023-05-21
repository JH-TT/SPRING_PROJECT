package com.mysite.sbb.api;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.QuestionListDTO;
import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.QuestionForm;
import com.mysite.sbb.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @RequestBody QuestionForm questionForm, HttpServletRequest request) {
        QuestionDTO questionDTO = questionService.getQuestion(id);
        SessionUser sessionUser = getSessionUser(request);
        userNameValidation(sessionUser, questionDTO, "수정 권한이 없습니다" + questionDTO.getSubject());
        QuestionDTO updatedQuestion = questionService.modify(id, questionForm.getSubject(), questionForm.getContent());
        return ResponseEntity.ok().body(updatedQuestion);
    }

    private void userNameValidation(SessionUser sessionUser, QuestionDTO questionDTO, String message) {
        if(!(questionDTO.getAuthor().getUsername().equals(sessionUser.getName()) || questionDTO.getAuthor().getRole() == UserRole.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private SessionUser getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (SessionUser) session.getAttribute("user");
    }
}
