package com.kakao.sunsuwedding.quotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.security.SecurityConfig;
import com.kakao.sunsuwedding.match.Quotation.QuotationRequest;
import com.kakao.sunsuwedding.user.UserRequest;
import com.kakao.sunsuwedding.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@Import({
        SecurityConfig.class
})
@Sql("classpath:/db/teardown.sql")
@SpringBootTest
public class QuotationControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private String plannerToken;


    public QuotationControllerTest(@Autowired MockMvc mockMvc,
                                   @Autowired ObjectMapper objectMapper,
                                   @Autowired UserService userService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @BeforeEach
    void beforeEach() {
        UserRequest.LoginDTO request = new UserRequest.LoginDTO();
        request.setEmail("planner@gmail.com");
        request.setPassword("planner1234!");
        plannerToken = userService.login(request);
    }

    @DisplayName("POST /quotations : success")
    @Test
    void post_quotations_success() throws Exception {
        // given
        Long matchId = 1L;
        QuotationRequest.addQuotation request = new QuotationRequest.addQuotation(
                "my wedding",
                1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("GET /quotations : success")
    @Test
    void get_quotations_success() throws Exception {
        // given
        Long matchId = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response.status").value("미완료"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].title").value("test"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].price").value("1000000"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].company").value("abc"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].description").value("asdf"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].status").value("미완료"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].title").value("test2"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].price").value("1000000"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].company").value("abc2"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].description").value("asdf2"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].status").value("미완료"));
    }
}
