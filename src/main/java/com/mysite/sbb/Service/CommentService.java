package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.CommentDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Answer;

public interface CommentService {
    void create(AnswerDTO answerDTO, String content, SiteUserDTO siteUserDTO);
    CommentDTO getComment(Long id);
    void modify(CommentDTO commentDTO, String content);
    void delete(CommentDTO commentDTO);
    void vote(CommentDTO commentDTO, SiteUserDTO siteUserDTO);
}
