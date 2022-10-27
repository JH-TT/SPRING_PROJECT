package com.mysite.sbb.Repository;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.Model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // findBy + 엔티티의 속성명
    QuestionDTO findBySubject(String subject);
    QuestionDTO findBySubjectAndContent(String subject, String content);

    // 제목에 특정 문자열이 포함되어 있는 데이터 조회.
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
}
