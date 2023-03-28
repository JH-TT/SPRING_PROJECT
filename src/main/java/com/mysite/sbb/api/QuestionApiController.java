package com.mysite.sbb.api;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.QuestionListDTO;
import com.mysite.sbb.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionApiController {

    private final QuestionService questionService;

    @GetMapping("/question/getlist")
    public Page<QuestionListDTO> getQuestionList() {
        return questionService.getListV1(0, "");
    }

    @GetMapping("/question/details/{id}")
    public QuestionDTO getQuestion(@PathVariable("id") Long id) {
        return questionService.getQuestion(id);
    }
}
