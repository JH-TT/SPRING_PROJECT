package com.mysite.sbb.Controller;

import com.mysite.sbb.AnswerForm;
import com.mysite.sbb.CommentForm;
import com.mysite.sbb.DTO.*;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.CommentService;
import com.mysite.sbb.Service.QuestionService;
import com.mysite.sbb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final AnswerService answerService;
    private final CommentService commentService;
    private final QuestionService questionService;

    @PostMapping("/create/{id}")
    @PreAuthorize("isAuthenticated()")
    public String createComment(Model model, @PathVariable("id") Long id
                                , @RequestParam String content, BindingResult bindingResult, HttpServletRequest request) {
        AnswerDTO answerDTO = answerService.getAnswer(id);
        SessionUser sessionUser = getSessionUser(request);
        if (bindingResult.hasErrors()) {
            QuestionDTO questionDTO = questionService.getQuestion(answerDTO.getQuestion());
            model.addAttribute("question", questionDTO);
            return "question/question_detail";
        }
        if (!sessionUser.isEmailCheck()) {
            return "redirect:/user/resendEmail";
        }
        // 바인딩 오류 추가예정
        commentService.create(id, content, sessionUser.getName());
        return String.format("redirect:/question/detail/%s#answer_%s", answerDTO.getQuestion(), answerDTO.getId());
    }

    // 보통 html에서 href로 요청하는건 get방식인듯...
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{questionId}/{commentId}")
    public String commentModify(CommentForm commentForm, @PathVariable("questionId") Long questionId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        CommentDTO commentDTO = commentService.getComment(commentId);
        SessionUser sessionUser = getSessionUser(request);
        userNameValidation(sessionUser, commentDTO, "수정 권한이 없습니다.");
        commentForm.setContent(commentDTO.getContent());
        return "/comment/comment_form";
    }

    // 수정하고 제출할때는 post방식으로 넘어온다.
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{questionId}/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("questionId") Long questionId, @PathVariable("id") Long id,
                                HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "/comment/comment_form";
        }
        CommentDTO commentDTO = commentService.getComment(id);
        SessionUser sessionUser = getSessionUser(request);
        userNameValidation(sessionUser, commentDTO, "수정 권한이 없습니다.");
        commentService.modify(commentDTO, commentForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", questionId, commentDTO.getAnswer());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{questionId}/{id}")
    public String commentDelete(HttpServletRequest request, @PathVariable("questionId") Long questionId, @PathVariable("id") Long id) {
        CommentDTO commentDTO = commentService.getComment(id);
        SessionUser sessionUser = getSessionUser(request);
        userNameValidation(sessionUser, commentDTO, "수정 권한이 없습니다.");
        commentService.delete(id);
        return String.format("redirect:/question/detail/%s#answer_%s", questionId, commentDTO.getAnswer());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{questionId}/{id}")
    public String commentVote(HttpServletRequest request, @PathVariable("questionId") Long questionId, @PathVariable("id") Long id) {
        CommentDTO commentDTO = commentService.getComment(id);
        SessionUser sessionUser = getSessionUser(request);
        commentService.vote(id, sessionUser.getName());
        return String.format("redirect:/question/detail/%s#answer_%s", questionId, commentDTO.getAnswer());
    }

    private void userNameValidation(SessionUser sessionUser, CommentDTO commentDTO, String message) {
        if(!(commentDTO.getAuthor().getUsername().equals(sessionUser.getName()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private SessionUser getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (SessionUser) session.getAttribute("user");
    }
}
