package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;

public interface AnswerService {
    Answer create(Question question, String content, SiteUserDTO author);
    Answer getAnswer(Integer id);
    Integer getQuestionId(Integer id);
    void modify(Answer answer, String content);
    void delete(Answer answer);
    void vote(Answer answer, SiteUserDTO siteUserDTO);
}
