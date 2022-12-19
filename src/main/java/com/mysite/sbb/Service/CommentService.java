package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Answer;

public interface CommentService {
    void create(AnswerDTO answerDTO, String content, SiteUserDTO siteUserDTO);
}
