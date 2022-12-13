package com.mysite.sbb.Repository;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.Model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // findBy + 엔티티의 속성명
    QuestionDTO findBySubject(String subject);
    QuestionDTO findBySubjectAndContent(String subject, String content);

    // N+1 문제를 해결하기 위함
    // 그냥 join fetch로 하니 질문에 답변이 없으면 null을 뱉어버림. 그래서 left 추가.
    @Query("select q from Question q left join fetch q.answerList where :id = q.id")
    Optional<Question> findById(Integer id);
    // 제목에 특정 문자열이 포함되어 있는 데이터 조회.
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
}
