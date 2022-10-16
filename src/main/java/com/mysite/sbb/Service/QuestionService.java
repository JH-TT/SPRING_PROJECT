package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getList();
    Question getQuestion(Integer id);
    void create(String subject, String content);
}
