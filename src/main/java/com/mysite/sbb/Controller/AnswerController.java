package com.mysite.sbb.Controller;

import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Service.AnswerService;
import com.mysite.sbb.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id
                                ,@RequestParam String content){
        Question question = questionService.getQuestion(id);
        answerService.create(question, content);
        return String.format("redirect:/question/detail/%s", id);
    }
}
