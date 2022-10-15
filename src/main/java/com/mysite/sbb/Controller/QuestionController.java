package com.mysite.sbb.Controller;

import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<Question> questionList = questionService.getList();
        // questionList라는 이름으로 질문목록을 model객체에 저장한다.
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
