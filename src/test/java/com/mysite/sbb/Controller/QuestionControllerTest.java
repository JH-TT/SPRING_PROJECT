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
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
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

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("addQuestion : 게시글 추가에 성공한다.")
    @Test
    public void addQuestion() throws Exception {
        // given
        final String url = "/api/questions";
        final String title = "title";
        final String content = "content";
        final QuestionForm questionForm = new QuestionForm(title, content);
        SiteUser siteUser = userService.create("ttt", "ttt@test.com", "1234");

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

        // 1개가 존재하고 제목과 내용이 같다.
        assertThat(questions.size()).isEqualTo(1);
        assertThat(questions.get(0).getSubject()).isEqualTo(title);
        assertThat(questions.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("특정 게시글을 가져온다.")
    @Test
    public void getQuestion() throws Exception {
        // given
        SiteUser siteUser = userService.create("ttt", "ttt@test.com", "1234");
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


    @DisplayName("특정 게시글을 삭제한다.")
    @Test
    @WithMockUser("ttt")
    public void deleteQuestion() throws Exception {
        // given

        // 세션 생성
        SiteUser siteUser = userService.create("ttt", "ttt@test.com", "1234");
        session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(siteUser));
        // 세션 생성 END

        Question savedQuestion = questionRepository.save(new Question("하이요", "제목", siteUser));
        final String url = "/question/delete/{id}";

        // when
        ResultActions result = mockMvc.perform(get(url, savedQuestion.getId()).session(session));

        // then
        result.andExpect(status().is3xxRedirection()); // delete된 다음 루트 url로 리다이렉트함

        List<Question> questions = questionRepository.findAll();

        assertThat(questions.size()).isEqualTo(0); // 삭제되면 남는 게시글이 없어야 한다. 즉, size가 0이어야 한다.
    }
}