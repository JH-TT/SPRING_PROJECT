package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.CommentDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Comment;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.AnswerRepository;
import com.mysite.sbb.Repository.CommentRepository;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Override
    public Long create(final Long id, final String content, final String username) {
        Answer answer = answerRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 댓글이 존재하지 않습니다.")
        );
        SiteUser siteUser = userRepository.findByusername(username).orElseThrow(
                () -> new DataNotFoundException("해당 유저가 존재하지 않습니다.")
        );
        Comment comment = Comment.createComment(answer, content, siteUser);
        return commentRepository.save(comment).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDTO getComment(final Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            return CommentDTO.from(comment.get());
        } else {
            throw new DataNotFoundException("Comment not found");
        }
    }

    @Override
    public void modify(CommentDTO commentDTO, final String content) {
        Comment comment = commentRepository.findById(commentDTO.getId()).orElseThrow(
                () -> new DataNotFoundException("해당 대댓글이 존재하지 않습니다")
        );
        comment.updateContent(content);
    }

    @Override
    public void delete(final Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 대댓글이 존재하지 않습니다.")
        );
        commentRepository.delete(comment);
    }

    @Override
    public void vote(final Long id, final String username) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 대댓글이 존재하지 않습니다.")
        );
        SiteUser siteUser = userRepository.findByusername(username).orElseThrow(
                () -> new DataNotFoundException("해당 유저가 존재하지 않습니다.")
        );
        comment.vote(siteUser);
    }
}
