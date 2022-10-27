package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<>() {

            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복 제거.
                // q : Root, 즉 기준을 의미하는 Question 엔티티의 객체(질문 제목과 내용을 검색하기 위해 필요)

                // u1 : Question엔티티와 SiteUser 엔티티를 아우터 조인하여 만든 SiteUser 엔티티의 객체
                // Question 엔티티와 SiteUser 엔티티는 author 속성으로 연결되어 있기 때문에 q.join("author") 와 같이 조인해야 한다.(질문 작성자를 검색하기 위해 필요.)
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);

                // a : Question 엔티티와 Answer 엔티티를 아우터 조인해 만든 Answer 엔티티의 객체.
                // Question엔티티와 Answer엔티티는 answerList 속성으로 연결되어 있어서 q.join("answerList")와 같이 조인해야 한다.(답변 내용을 검색하기 위해 필요.)
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);

                // u2 : 바로 위에서 작성한 a객체와 다시 한 번 SiteUser 엔티티와 아우터 조인해 만든 SiteUser 엔티티의 객체.(답변 작성자를 검색하기 위해 필요.)
                Join<Question, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"),
                        cb.like(q.get("content"), "%" + kw + "%"),
                        cb.like(u1.get("username"), "%" + kw + "%"),
                        cb.like(a.get("content"), "%" + kw + "%"),
                        cb.like(u2.get("username"), "%" + kw + "%"));
            };
        };
    }
    @Override
    public List<Question> getList() {
        return questionRepository.findAll();
    }

    @Override
    public Page<Question> getList(int page) {
        // 최근날짜로 데이터 가져옴
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        // page : 조회할 번호, size : 몇개씩 가져올건지
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);
    }

    @Override
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return questionRepository.findAll(spec, pageable);
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

    @Override
    public void create(String subject, String content, SiteUserDTO author) {
        Question q = new Question(subject, content, author.toEntity());
        questionRepository.save(q);
    }

    @Override
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        questionRepository.save(question);
    }

    @Override
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    @Override
    public void vote(Question question, SiteUserDTO siteUserDTO) {
        question.getVoter().add(siteUserDTO.toEntity());
        questionRepository.save(question);
    }

}
