package com.mysite.sbb.Service;

import com.mysite.sbb.DTO.QuestionDTO;
import com.mysite.sbb.DTO.SiteUserDTO;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.QuestionRepository;
import com.mysite.sbb.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

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
    public List<QuestionDTO> getList() {
        return questionRepository.findAll()
                .stream().map(QuestionDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public Page<QuestionDTO> getList(int page) {
        // 최근날짜로 데이터 가져옴
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createDate"));
        // page : 조회할 번호, size : 몇개씩 가져올건지
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createDate"));
        return QuestionDTO.toDtoList(questionRepository.findAll(pageable));
    }

    @Override
    public Page<QuestionDTO> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return QuestionDTO.toDtoList(questionRepository.findAll(spec, pageable));
    }

    @Override
    public QuestionDTO getQuestion(Long id) {
        Question q = questionRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("question not found")
        );
        return QuestionDTO.from(q);
    }

    @Override
    @Transactional
    public void create(String subject, String content, SiteUserDTO author) {
        Question question = new Question(subject, content, author);
        questionRepository.save(question);
    }

    @Override
    @Transactional
    public void modify(Long id, String subject, String content) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 질문글이 존재하지 않습니다.")
        );
        question.updateQuestion(subject, content);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("해당 질문이 존재하지 않습니다.")
        );
        questionRepository.delete(question);
    }


    @Override
    @Transactional
    public void vote(Long id, String username) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("존재하지 않는 게시글 입니다.")
        );
        SiteUser siteUser = userRepository.findByusername(username).orElseThrow(
                () -> new DataNotFoundException("존재하지 않는 유저입니다.")
        );
        question.addRecommand(siteUser);
    }
}
