package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.QuestionListDTO;
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
    Page<QuestionListDTO> getListV1(int page, String kw);
    QuestionDTO getQuestion(Long id);
    Long create(String subject, String content, String email);
    QuestionDTO modify(Long id, String subject, String content);
    void delete(Long id);
    void vote(Long id, String username);
    boolean checkLiked(Long id, String user);
}
