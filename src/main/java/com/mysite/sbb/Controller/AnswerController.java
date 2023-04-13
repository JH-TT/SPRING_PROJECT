package com.mysite.sbb.Controller;

import com.mysite.sbb.AnswerForm;
import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.Enum.UserRole;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.QuestionService;
import com.mysite.sbb.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id
                                , @Valid AnswerForm answerForm, BindingResult bindingResult, HttpServletRequest request){
        QuestionDTO questionDTO = questionService.getQuestion(id);
        SessionUser sessionUser = getSessionUser(request);
        log.info("siteUser name = {}", sessionUser.getName());
        log.info("siteUser email = {}", sessionUser.getEmail());
        if(bindingResult.hasErrors()) {
            model.addAttribute("question", questionDTO);
            return "/question/question_detail";
        }
        AnswerDTO answerDTO = answerService.create(id, answerForm.getContent(), sessionUser.getEmail());
        return String.format("redirect:/question/detail/%s#answer_%s", id, answerDTO.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Long id, HttpServletRequest request) {
        AnswerDTO answer = answerService.getAnswer(id);
        SessionUser sessionUser = getSessionUser(request);
        userNameValidation(sessionUser, answer, "수정 권한이 없습니다.");
        answerForm.setContent(answer.getContent());
        return "/answer/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult
        ,@PathVariable("id") Long id, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "/answer/answer_form";
        }
        AnswerDTO answerDTO = answerService.getAnswer(id);
        SessionUser sessionUser = getSessionUser(request);
        userNameValidation(sessionUser, answerDTO, "수정 권한이 없습니다");
        answerService.modify(answerDTO, answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", answerDTO.getQuestion(), answerDTO.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(HttpServletRequest request, @PathVariable("id") Long id) {
        AnswerDTO answer = answerService.getAnswer(id);
        SessionUser sessionUser = getSessionUser(request);
        userNameValidation(sessionUser, answer, "삭제 권한이 없습니다");
        answerService.delete(id);
        return String.format("redirect:/question/detail/%s", answer.getQuestion());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(HttpServletRequest request, @PathVariable("id") Long id) {
        AnswerDTO answer = answerService.getAnswer(id);
        SessionUser sessionUser = getSessionUser(request);
        answerService.vote(id, sessionUser.getName());
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion(), answer.getId());
    }

    private void userNameValidation(SessionUser sessionUser, AnswerDTO answerDTO, String message) {
        if(!(answerDTO.getAuthor().getUsername().equals(sessionUser.getName()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private SessionUser getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (SessionUser) session.getAttribute("user");
    }
}
