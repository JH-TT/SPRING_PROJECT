package com.mysite.sbb.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    @Override
    public List<Question> getList() {
        return questionRepository.findAll();
    }

    @Override
    public Question getQuestion(Integer id) {
        Optional<Question> question = questionRepository.findById(id);
        if(question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }
}
