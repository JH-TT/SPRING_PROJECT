package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface QuestionService {
    List<Question> getList();
    Page<Question> getList(int page);
    Page<Question> getList(int page, String kw);
    Question getQuestion(Integer id);
    void create(String subject, String content, SiteUserDTO author);
    void modify(Question question, String subject, String content);
    void delete(Question question);
    void vote(Question question, SiteUserDTO siteUserDTO);
}
