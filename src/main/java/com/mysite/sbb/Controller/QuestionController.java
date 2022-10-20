package com.mysite.sbb.Controller;

import com.mysite.sbb.AnswerForm;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.QuestionForm;
import com.mysite.sbb.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

//    @RequestMapping("/list")
//    public String list(Model model) {
//        List<Question> questionList = questionService.getList();
//        // questionList라는 이름으로 질문목록을 model객체에 저장한다.
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }

    // 스프링부트의 페이징은 첫페이지 번호가 1이 아닌 0이다.
    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> paging = questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id
                            , AnswerForm answerForm) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

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
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        // 오류가 있는 경우엔 다시 폼을 작성하는 화면을 렌더링하게한다.
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }
}
