package com.mysite.sbb;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Comment;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.AnswerRepository;
import com.mysite.sbb.Repository.CommentRepository;
import com.mysite.sbb.Repository.QuestionRepository;
import com.mysite.sbb.Repository.UserRepository;
import com.mysite.sbb.Service.QuestionService;
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
    private QuestionService questionService;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

//    public SbbApplicationTests(QuestionRepository questionRepository) {
//        this.questionRepository = questionRepository;
//    }

    @Test
    void contextLoads() {
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다");

        SiteUser st = new SiteUser("aa", "1234", "lee@gmail.com");
        q1.setAuthor(st);

        questionRepository.save(q1);

//        Question q2 = Question.builder()
//                .content("안녕")
//                .subject("안녕2")
//                .build();
//        questionRepository.save(q2);
//
//        try{
//            Thread.sleep(1000);
//            updateq(q2);
//        } catch(InterruptedException e) {
//            System.out.println(e);
//        }
    }

//    @Test
//    void testJpa() {
//        // Optional은 null을 좀 더 유연하게 처리하기 위함.
//        Optional<Question> oq = questionRepository.findById(5);
//        if(oq.isPresent()) {
//            Question q = oq.get();
//            assertEquals("sbb가 무엇인가요?", q.getSubject());
//        }
//
//        // findbysubject
//        Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
//        assertEquals(5, q.getId());
//
//        // findbysubjectlike
//        List<Question> qList = questionRepository.findBySubjectLike("sbb%");
//        Question q2 = qList.get(0);
//        assertEquals("sbb가 무엇인가요?", q.getSubject());
//
//        // 데이터 삭제하기
//        Optional<Question> oq2 = questionRepository.findById(5);
//        assertTrue(oq2.isPresent());
//        Question q3 = oq.get();
//        questionRepository.delete(q3);
//    }

    @Test
    void testJpa2() {
        Optional<Question> oq = questionRepository.findById(18);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다");
        a.setQuestion(q);
        answerRepository.save(a);

        Comment c = Comment.builder()
                .content("hi")
                .answer(a)
                .build();
        commentRepository.save(c);
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
    void testJpa4() {
        for(int i=1; i<= 100; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            questionService.create(subject, content, SiteUserDTO.from(userRepository.findByusername("dlwhdgh98").get()));
        }
    }

    @Test
    void te() {
        SiteUser st = new SiteUser("John2", "1234", "leejongho9807@gmail.com");
        userRepository.save(st);
        Question q = new Question("Hi", "Hi", st);
        questionRepository.save(q);
    }

    @Test
    void updateq(Question q){
        q.setContent("안녕3");
        questionRepository.save(q);
    }

}
