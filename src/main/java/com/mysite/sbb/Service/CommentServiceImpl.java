package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.CommentDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.Comment;
import com.mysite.sbb.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;


    @Override
    public void create(AnswerDTO answerDTO, String content, SiteUserDTO siteUserDTO) {
        CommentDTO commentDTO = CommentDTO.builder()
                .answer(answerDTO.toEntity())
                .content(content)
                .author(siteUserDTO.toEntity())
                .build();
        commentRepository.save(commentDTO.toEntity());
    }

    @Override
    public CommentDTO getComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            return CommentDTO.from(comment.get());
        } else {
            throw new DataNotFoundException("Comment not found");
        }
    }

    @Override
    public void modify(CommentDTO commentDTO, String content) {
        commentDTO.setContent(content);
        commentRepository.save(commentDTO.toEntity());
    }

    @Override
    public void delete(CommentDTO commentDTO) {
        commentRepository.delete(commentDTO.toEntity());
    }

    @Override
    public void vote(CommentDTO commentDTO, SiteUserDTO siteUserDTO) {
        commentDTO.getVoter().add(siteUserDTO.toEntity());
        commentRepository.save(commentDTO.toEntity());
    }
}
