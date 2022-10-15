package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    @Override
    public void create(Question question, String content) {
        Answer answer = new Answer(content, question);
        answerRepository.save(answer);
    }
}
