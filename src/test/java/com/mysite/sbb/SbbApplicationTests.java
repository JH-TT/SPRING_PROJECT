package com.mysite.sbb;

import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Repository.AnswerRepository;
import com.mysite.sbb.Repository.QuestionRepository;
import org.hibernate.boot.model.relational.QualifiedSequenceName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

//    public SbbApplicationTests(QuestionRepository questionRepository) {
//        this.questionRepository = questionRepository;
//    }

    @Test
    void contextLoads() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다");
        questionRepository.save(q1);

        Question q2 = Question.builder()
                .content("안녕")
                .subject("안녕2")
                .build();
        questionRepository.save(q2);

        try{
            Thread.sleep(1000);
            updateq(q2);
        } catch(InterruptedException e) {
            System.out.println(e);
        }
    }

    @Test
    void testJpa() {
        // Optional은 null을 좀 더 유연하게 처리하기 위함.
        Optional<Question> oq = questionRepository.findById(5);
        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }

        // findbysubject
        Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(5, q.getId());

        // findbysubjectlike
        List<Question> qList = questionRepository.findBySubjectLike("sbb%");
        Question q2 = qList.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());

        // 데이터 삭제하기
        Optional<Question> oq2 = questionRepository.findById(5);
        assertTrue(oq2.isPresent());
        Question q3 = oq.get();
        questionRepository.delete(q3);
    }

    @Test
    void testJpa2() {
        Optional<Question> oq = questionRepository.findById(18);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다");
        a.setQuestion(q);
        answerRepository.save(a);
    }

    // DB세션을 유지하기 위함
    @Transactional
    @Test
    void testJpa3() {
        Optional<Question> oq = questionRepository.findById(18);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다", answerList.get(0).getContent());
    }

    @Test
    void updateq(Question q){
        q.setContent("안녕3");
        questionRepository.save(q);
    }

}