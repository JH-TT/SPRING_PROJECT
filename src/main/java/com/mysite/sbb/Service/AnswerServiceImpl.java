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
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public AnswerDTO create(Long id, String content, SiteUserDTO author) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 질문이 존재하지 않습니다.")
        );
        Answer answer = Answer.createAnswer(content, question, author);

        return AnswerDTO.from(answerRepository.save(answer));
    }

    // 유저엔티티가 넘어올 경우
    @Override
    public AnswerDTO create(Long id, String content, SiteUser author) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 질문이 존재하지 않습니다.")
        );
        Answer answer = Answer.createAnswer(content, question, author);
        return AnswerDTO.from(answerRepository.save(answer));
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
    @Transactional
    public void modify(AnswerDTO answerDTO, String content) {
        answerDTO.setContent(content);
        answerRepository.save(answerDTO.toEntity());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 댓글이 존재하지 않습니다.")
        );
        Question question = questionRepository.findById(answer.getQuestion().getId()).orElseThrow(
                () -> new DataNotFoundException("해당 게시글이 존재하지 않습니다.")
        );
        question.removeAnswer(answer);
        answerRepository.delete(answer);
    }

    @Override
    @Transactional
    public void vote(Long id, String username) {
        Answer answer = answerRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 댓글이 존재하지 않습니다.")
        );
        SiteUser siteUser = userRepository.findByusername(username).orElseThrow(
                () -> new DataNotFoundException("해당 유저가 존재하지 않습니다.")
        );
        answer.vote(siteUser);
    }
}
