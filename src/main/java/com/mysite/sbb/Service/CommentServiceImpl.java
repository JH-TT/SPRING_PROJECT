package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Comment;
import com.mysite.sbb.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;


    @Override
    public void create(Answer answer, String content) {
        Comment c = Comment.builder()
                .content(content)
                .answer(answer)
                .build();
        commentRepository.save(c);
    }
}
