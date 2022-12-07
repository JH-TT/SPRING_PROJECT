package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Answer;

public interface CommentService {
    void create(Answer answer, String content);
}
