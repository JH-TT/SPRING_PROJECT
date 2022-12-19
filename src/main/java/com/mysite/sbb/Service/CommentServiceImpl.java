package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.CommentDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
