package com.mysite.sbb.Controller;

import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionRepository questionRepository;

    @RequestMapping("/question/list")
    public String list(Model model) {
        List<Question> questionList = questionRepository.findAll();
        // questionList라는 이름으로 질문목록을 model객체에 저장한다.
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
