package com.mysite.sbb.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.QuestionForm;
import com.mysite.sbb.Repository.QuestionRepository;
import com.mysite.sbb.Repository.UserRepository;
import com.mysite.sbb.Service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public MockHttpSession session;
    public SiteUser siteUser;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        questionRepository.deleteAll();
        userRepository.deleteAll();

        siteUser = userService.create("ttt", "ttt@test.com", "1234");
        session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(siteUser));
    }

    @DisplayName("addQuestion : 게시글 추가에 성공한다.")
    @Test
    public void addQuestion() throws Exception {
        // given
        final String url = "/api/questions";
        final String title = "title";
        final String content = "content";
        final QuestionForm questionForm = new QuestionForm(title, content);

        // 객체 json으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(questionForm);

        // when
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
                .param("email", siteUser.getEmail()));

        // then
        result.andExpect(status().isOk());

        List<Question> questions = questionRepository.findAll();

        // 2개가 존재하고(미리 게시글을 만들기 때문) 제목과 내용이 같다.
        assertThat(questions.size()).isEqualTo(1);
        assertThat(questions.get(0).getSubject()).isEqualTo(title);
        assertThat(questions.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("특정 게시글을 가져온다.")
    @Test
    public void getQuestion() throws Exception {
        // given
        Question savedQuestion = questionRepository.save(new Question("하이요", "제목", siteUser));
        final String url = "/question/details/" + savedQuestion.getId();

        // when
        ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedQuestion.getId()))
                .andExpect(jsonPath("$.content").value(savedQuestion.getContent()));
    }


    @DisplayName("Soft Delete와 Where테스트")
    @Test
    public void deleteAndWhere() throws Exception {
        // given
        Question savedQuestion = questionRepository.save(new Question("하이요", "제목", siteUser));

        // when
        questionRepository.delete(savedQuestion);
        entityManager.flush();

        // then
        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());
        assertThat(findQuestion).isEmpty();
    }

    /*@DisplayName("특정 게시글을 삭제한다.")
    @Test
    @WithMockUser("ttt")
    public void deleteQuestion() throws Exception {
        // given
        final String url = "/question/delete/{id}";
        Question savedQuestion = questionRepository.save(new Question("하이요", "제목", siteUser));

        // when
        ResultActions result = mockMvc.perform(get(url, savedQuestion.getId()).session(session));

        // then
        result.andExpect(status().is3xxRedirection()); // delete된 다음 루트 url로 리다이렉트함

        List<Question> questions = questionRepository.findAll();

        assertThat(questions.size()).isEqualTo(0); // 삭제되면 남는 게시글이 없어야 한다. 즉, size가 0이어야 한다.
    }*/

    @DisplayName("특정 게시글을 수정한다")
    @Test
    @WithMockUser("ttt")
    public void updateQuestion() throws Exception {
        // given
        Question savedQuestion = questionRepository.save(new Question("하이요", "제목", siteUser));

        final String url = "/question/modify/{id}";
        final String title = "제목수정됨";
        final String content = "내용수정됨";

        // ModelAttribute의 경우 MultiValueMap을 이용해서 폼형식으로 보낸다.
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("subject", title);
        request.add("content", content);

        // when
        ResultActions result = mockMvc.perform(post(url, savedQuestion.getId()).session(session)
                .params(request)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print());

        // then
        result.andExpect(status().is3xxRedirection());

        Question question = questionRepository.findById(savedQuestion.getId()).get();

        assertThat(question.getSubject()).isEqualTo(title);
        assertThat(question.getContent()).isEqualTo(content);
    }

    @DisplayName("특정 게시글을 수정한다. - API")
    @Test
    @WithMockUser("ttt")
    public void updateQuestionApi() throws Exception {
        // given
        Question savedQuestion = questionRepository.save(new Question("하이요", "제목", siteUser));

        final String url = "/question/{id}";
        final String title = "제목수정됨";
        final String content = "내용수정됨";

        QuestionForm request = new QuestionForm(title, content); // 수정 폼 작성

        // when
        ResultActions result = mockMvc.perform(post(url, savedQuestion.getId()).session(session)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk());

        Question question = questionRepository.findById(savedQuestion.getId()).get();

        assertThat(question.getSubject()).isEqualTo(title);
        assertThat(question.getContent()).isEqualTo(content);
    }
}