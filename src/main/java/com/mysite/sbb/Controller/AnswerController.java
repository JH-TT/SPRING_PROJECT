package com.mysite.sbb.Controller;

import com.mysite.sbb.AnswerForm;
import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.PrincipalDetails;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.QuestionService;
import com.mysite.sbb.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public String createAnswer(Model model, @PathVariable("id") Integer id
                                , @Valid AnswerForm answerForm, BindingResult bindingResult, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal, Principal principal){
        QuestionDTO questionDTO = questionService.getQuestion(id);
        SiteUserDTO siteUserDTO;
        if(oAuth2UserPrincipal == null) {
            siteUserDTO = userService.getUser(principal.getName());
        } else {
            siteUserDTO = userService.getUser(((PrincipalDetails) oAuth2UserPrincipal).getUser().getUsername());
        }
        log.info("siteUserDTO={}", siteUserDTO.getUsername());

        if(bindingResult.hasErrors()) {
            model.addAttribute("question", questionDTO);
            return "/question/question_detail";
        }
        AnswerDTO answerDTO = answerService.create(questionDTO, answerForm.getContent(), siteUserDTO);
        return String.format("redirect:/question/detail/%s#answer_%s", answerDTO.getQuestion().getId(), answerDTO.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        AnswerDTO answerDTO = answerService.getAnswer(id);
        if(!answerDTO.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answerDTO.getContent());
        return "/answer/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult
        ,@PathVariable("id") Integer id, Principal principal) {
        if(bindingResult.hasErrors()) {
            return "/answer/answer_form";
        }
        AnswerDTO answerDTO = answerService.getAnswer(id);
        if(!answerDTO.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerService.modify(answerDTO, answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", answerDTO.getQuestion().getId(), answerDTO.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        AnswerDTO answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        AnswerDTO answer = answerService.getAnswer(id);
        SiteUserDTO siteUserDTO = userService.getUser(principal.getName());
        answerService.vote(answer, siteUserDTO);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
}
