package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;

public interface AnswerService {
    void create(Question question, String content, SiteUserDTO author);
}
