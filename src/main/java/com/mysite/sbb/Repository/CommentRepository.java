package com.mysite.sbb.Repository;

import com.mysite.sbb.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
