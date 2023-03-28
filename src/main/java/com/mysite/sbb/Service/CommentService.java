package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.CommentDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Answer;

public interface CommentService {
    Long create(Long id, String content, String username);
    CommentDTO getComment(Long id);
    void modify(CommentDTO commentDTO, String content);
    void delete(CommentDTO commentDTO);
    void vote(CommentDTO commentDTO, SiteUserDTO siteUserDTO);
}
