package com.mysite.sbb.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.sbb.DTO.SessionUser;
import com.mysite.sbb.Model.Answer;
import com.mysite.sbb.Model.Question;
import com.mysite.sbb.Model.SiteUser;
import com.mysite.sbb.Repository.AnswerRepository;
import com.mysite.sbb.Repository.QuestionRepository;
import com.mysite.sbb.Repository.UserRepository;
import com.mysite.sbb.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public MockHttpSession session;
    public SiteUser siteUser;

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();

        siteUser = userService.create("ttt", "ttt@test.com", "1234");
        session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(siteUser));
    }

    @DisplayName("addAnswer : 댓글을 작성한다.")
    @Test
    @WithMockUser("ttt")
    public void addAnswer() throws Exception {
        // given
        Question question = questionRepository.save(new Question("제목", "내용", siteUser));
        final String url = "/answer/create/{id}";
        final String content = "댓글입니다.";

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("content", content);

        // when
        ResultActions result = mockMvc.perform(post(url, question.getId()).session(session)
                        .params(request)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        result.andExpect(status().is3xxRedirection());

        List<Answer> answerList = answerRepository.findAll();

        assertThat(answerList.size()).isEqualTo(1);
        assertThat(answerList.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("특정 댓글을 삭제한다.")
    @Test
    @WithMockUser("ttt")
    public void deleteAnswer() throws Exception {
        // given
        final String url = "/answer/delete/{id}";
        Question question = questionRepository.save(new Question("제목", "내용", siteUser));
        Answer savedAnswer = answerRepository.save(Answer.createAnswer("댓글입니다.", question, siteUser));

        // when
        ResultActions result = mockMvc.perform(get(url, savedAnswer.getId()).session(session));

        // then
        result.andExpect(status().is3xxRedirection());
        List<Answer> answerList = answerRepository.findAll();

        assertThat(answerList.size()).isEqualTo(0);
    }

    @DisplayName("특정 댓글을 수정한다.")
    @Test
    @WithMockUser("ttt")
    public void modifyAnswer() throws Exception {
        // given
        final String url = "/answer/modify/{id}";
        final String newContent = "수정된 댓글입니다.";
        Question question = questionRepository.save(new Question("제목", "내용", siteUser));
        Answer savedAnswer = answerRepository.save(Answer.createAnswer("댓글입니다.", question, siteUser));

        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("content", newContent);

        // when
        ResultActions result = mockMvc.perform(post(url, savedAnswer.getId()).session(session)
                .params(request)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        result.andExpect(status().is3xxRedirection());
        Answer modifiedAnswer = answerRepository.findById(savedAnswer.getId()).get();

        assertThat(modifiedAnswer.getContent()).isEqualTo(newContent);
    }
}
