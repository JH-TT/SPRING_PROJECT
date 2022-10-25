package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    @Override
    public void create(Question question, String content, SiteUserDTO author) {
        Answer answer = new Answer(content, question, author.toEntity());
        answerRepository.save(answer);
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
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answerRepository.save(answer);
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
