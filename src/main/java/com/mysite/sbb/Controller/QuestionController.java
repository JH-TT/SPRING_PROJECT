package com.mysite.sbb.Controller;

import com.mysite.sbb.AnswerForm;
import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.QuestionForm;
import com.mysite.sbb.Service.QuestionService;
import com.mysite.sbb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    private final UserService userService;

//    @RequestMapping("/list")
//    public String list(Model model) {
//        List<Question> questionList = questionService.getList();
//        // questionList라는 이름으로 질문목록을 model객체에 저장한다.
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }

    // 스프링부트의 페이징은 첫페이지 번호가 1이 아닌 0이다.
    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<QuestionDTO> paging = questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id
                            , AnswerForm answerForm) {
        QuestionDTO questionDTO = questionService.getQuestion(id);
        model.addAttribute("question", questionDTO);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    // @GetMapping시 사용했던 questionCreate 메서드명과 동일하게 사용할 수 있다.
    // (단, 매개변수의 형태가 다른 경우에 가능하다. - 메서드 오버로딩)
    // questionCreate 메서드의 매개변수를 subject, content 대신 QuestionForm 객체로 변경.
    // subject, content 항목을 지닌 폼이 전송되면 QuestionForm의 subject, content 속성이 자동으로 바인딩 된다.
    // QuestionForm 매개변수 앞에 @Valid 애너테이션 적용 그러면 @NotEmpty 등의 검증 기능이 작동함.
    // BindingResult 매개변수는 @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체이다.
    // BindingResult는 항상 @Valid 매개변수 바로 뒤에 있어야 한다.
    // 만약 2개의 매개변수의 위치가 맞지 않으면 @Valid만 적용돼 값 검증 실패 시 400오류뜬다.
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        // 오류가 있는 경우엔 다시 폼을 작성하는 화면을 렌더링하게한다.
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUserDTO siteUserDTO = userService.getUser(principal.getName());
        questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUserDTO);
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        QuestionDTO questionDTO = questionService.getQuestion(id);
        if(!questionDTO.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        // 수정할 질문의 제목과 내용을 화면에 보여주기 위해 Form객체에 값을 담아서 템플릿으로 전달한다.
        // 이게 없으면 값이 채워지지 않는다.
        questionForm.setSubject(questionDTO.getSubject());
        questionForm.setContent(questionDTO.getContent());

        // 질문 등록 템플릿을 그대로 사용하면 질문이 수정되는 것이아닌, 새로운 질문이 올라가게 된다.
        // 그래서 템플릿 폼 태그의 action을 잘 활용하면 유연하게 대처할 수 있다.
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, @PathVariable("id") Integer id, BindingResult bindingResult
            , Principal principal) {
        if(bindingResult.hasErrors()) {
            return "question_form";
        }
        QuestionDTO questionDTO = questionService.getQuestion(id);
        if(!questionDTO.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionService.modify(questionDTO, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        QuestionDTO questionDTO = questionService.getQuestion(id);
        if(!questionDTO.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        questionService.delete(questionDTO);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        QuestionDTO questionDTO = questionService.getQuestion(id);
        SiteUserDTO siteUser = userService.getUser(principal.getName());
        questionService.vote(questionDTO, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
