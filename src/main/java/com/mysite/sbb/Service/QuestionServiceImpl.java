package com.mysite.sbb.Service;

import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    @Override
    public List<Question> getList() {
        return questionRepository.findAll();
    }
}
