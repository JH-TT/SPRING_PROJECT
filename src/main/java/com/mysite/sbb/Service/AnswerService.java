package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;

public interface AnswerService {
    AnswerDTO create(QuestionDTO questionDTO, String content, SiteUserDTO author);
    AnswerDTO getAnswer(Integer id);
    Integer getQuestionId(Integer id);
    void modify(AnswerDTO answerDTO, String content);
    void delete(AnswerDTO answerDTO);
    void vote(AnswerDTO answerDTO, SiteUserDTO siteUserDTO);
}
