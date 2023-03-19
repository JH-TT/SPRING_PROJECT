package com.mysite.sbb.Repository;

import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(value = "select a from Answer a left join fetch a.commentList where :id = a.id")
    List<Answer> findAll(@Param(value = "id") Long id);
}
