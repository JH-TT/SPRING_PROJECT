package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Question;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService {
    List<Question> getList();

    Page<Question> getList(int page);
    Question getQuestion(Integer id);
    void create(String subject, String content);
}
