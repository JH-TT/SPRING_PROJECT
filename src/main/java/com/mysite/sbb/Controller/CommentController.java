package com.mysite.sbb.Controller;

import com.mysite.sbb.CommentForm;
import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.CommentDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.CommentService;
import com.mysite.sbb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public String createComment(Model model, @PathVariable("id") Long id
                                , @RequestParam String content, @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
        AnswerDTO answerDTO = this.answerService.getAnswer(id);
        SiteUserDTO siteUserDTO = userService.getUser(principal.getName());
        // 바인딩 오류 추가예정
        commentService.create(answerDTO, content, siteUserDTO);
        return String.format("redirect:/question/detail/%s#answer_%s", answerDTO.getQuestion().getId(), answerDTO.getId());
    }

    // 보통 html에서 href로 요청하는건 get방식인듯...
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String commentModify(CommentForm commentForm, @PathVariable("id") Long id, Principal principal) {
        CommentDTO commentDTO = commentService.getComment(id);
        if(!commentDTO.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(commentDTO.getContent());
        return "/comment/comment_form";
    }

    // 수정하고 제출할때는 post방식으로 넘어온다.
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult
            ,@PathVariable("id") Long id, Principal principal) {
        if(bindingResult.hasErrors()) {
            return "/comment/comment_form";
        }
        CommentDTO commentDTO = commentService.getComment(id);
        if(!commentDTO.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentService.modify(commentDTO, commentForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", commentDTO.getAnswer().getQuestion().getId(), commentDTO.getAnswer().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String commentDelete(Principal principal, @PathVariable("id") Long id) {
        CommentDTO commentDTO = commentService.getComment(id);
        if(!commentDTO.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        commentService.delete(commentDTO);
        return String.format("redirect:/question/detail/%s#answer_%s", commentDTO.getAnswer().getQuestion().getId(), commentDTO.getAnswer().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String commentVote(Principal principal, @PathVariable("id") Long id) {
        CommentDTO commentDTO = commentService.getComment(id);
        SiteUserDTO siteUserDTO = userService.getUser(principal.getName());
        commentService.vote(commentDTO, siteUserDTO);
        return String.format("redirect:/question/detail/%s#answer_%s", commentDTO.getAnswer().getQuestion().getId(), commentDTO.getAnswer().getId());
    }
}
