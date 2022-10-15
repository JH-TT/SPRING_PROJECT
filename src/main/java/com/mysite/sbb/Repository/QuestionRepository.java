package com.mysite.sbb.Repository;

import com.mysite.sbb.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // findBy + 엔티티의 속성명
    Question findBySubject(String subject);

    // 제목에 특정 문자열이 포함되어 있는 데이터 조회.
    List<Question> findBySubjectLike(String subject);
}