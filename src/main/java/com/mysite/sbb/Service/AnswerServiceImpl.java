package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.AnswerDTO;
import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.AnswerRepository;
import com.mysite.sbb.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    @Override
    public AnswerDTO create(QuestionDTO questionDTO, String content, SiteUserDTO author) {
        AnswerDTO answerDTO = AnswerDTO.builder()
                .content(content)
                .question(questionDTO.toEntity())
                .author(author.toEntity())
                .build();
        answerRepository.save(answerDTO.toEntity());
        return answerDTO;
    }

    @Override
    public AnswerDTO getAnswer(Long id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if(answer.isPresent()) {
            return AnswerDTO.from(answer.get());
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    @Override
    public Long getQuestionId(Long id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if(answer.isPresent()) {
            return answer.get().getQuestion().getId();
        } else {
            throw new DataNotFoundException("Question not found");
        }
    }

    @Override
    public void modify(AnswerDTO answerDTO, String content) {
        answerDTO.setContent(content);
        answerRepository.save(answerDTO.toEntity());
    }

    @Override
    public void delete(AnswerDTO answerDTO) {
        answerRepository.delete(answerDTO.toEntity());
    }

    @Override
    public void vote(AnswerDTO answerDTO, SiteUserDTO siteUserDTO) {
        answerDTO.getVoter().add(siteUserDTO.toEntity());
        answerRepository.save(answerDTO.toEntity());
    }
}
