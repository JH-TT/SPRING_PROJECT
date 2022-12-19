package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface QuestionService {
    List<QuestionDTO> getList();
    Page<QuestionDTO> getList(int page);
    Page<QuestionDTO> getList(int page, String kw);
    QuestionDTO getQuestion(Integer id);
    void create(String subject, String content, SiteUserDTO author);
    void modify(QuestionDTO questionDTO, String subject, String content);
    void delete(QuestionDTO questionDTO);
    void vote(QuestionDTO questionDTO, SiteUserDTO siteUserDTO);
}
