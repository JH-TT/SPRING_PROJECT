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
    public Answer create(Question question, String content, SiteUserDTO author) {
        Answer answer = new Answer(content, question, author.toEntity());
        answerRepository.save(answer);
        return answer;
    }

    @Override
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if(answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    @Override
    public Integer getQuestionId(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if(answer.isPresent()) {
            return answer.get().getQuestion().getId();
        } else {
            throw new DataNotFoundException("Question not found");
        }
    }

    @Override
    public void modify(Answer answer, String content) {
        AnswerDTO answerDTO = AnswerDTO.from(answer);
        answerDTO.setContent(content);
        answerRepository.save(answerDTO.toEntity());
    }

    @Override
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    @Override
    public void vote(Answer answer, SiteUserDTO siteUserDTO) {
        answer.getVoter().add(siteUserDTO.toEntity());
        answerRepository.save(answer);
    }
}
