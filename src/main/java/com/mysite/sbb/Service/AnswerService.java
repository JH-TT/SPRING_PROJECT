package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;

public interface AnswerService {
    AnswerDTO create(Long id, String content, SiteUserDTO author);
    AnswerDTO getAnswer(Long id);
    Long getQuestionId(Long id);
    void modify(AnswerDTO answerDTO, String content);
    void delete(Long id);
    void vote(AnswerDTO answerDTO, SiteUserDTO siteUserDTO);
}
