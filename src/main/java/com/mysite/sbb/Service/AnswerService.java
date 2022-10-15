package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Question;

public interface AnswerService {
    void create(Question question, String content);
}
