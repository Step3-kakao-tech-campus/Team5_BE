package com.kakao.sunsuwedding.quotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.security.SecurityConfig;
import com.kakao.sunsuwedding.match.Quotation.QuotationRequest;
import com.kakao.sunsuwedding.user.UserRequest;
import com.kakao.sunsuwedding.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import({
        SecurityConfig.class,
})
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class QuotationRestControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(QuotationRestControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private String plannerToken;

    @BeforeEach
    void beforeEach() {
        UserRequest.LoginDTO request = new UserRequest.LoginDTO();
        request.setEmail("planner@gmail.com");
        request.setPassword("planner1234!");
        plannerToken = userService.login(request);
    }

    @DisplayName("전체 확정 업데이트 성공 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_confirm_all_success_test() throws Exception {
        //given
        Long matchId = 1L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations/confirmAll/1")
        );

        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
        System.out.println("테스트 : " + responseBody);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
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
        ResultActions resultActions = mvc.perform(
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

    @DisplayName("POST /quotations : fail, 제목 글자수 위반")
    @Test
    void post_quotations_fail_titleTextSize() throws Exception {
        // given
        Long matchId = 1L;
        QuotationRequest.addQuotation request = new QuotationRequest.addQuotation(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbb",
                1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /quotations : fail, 제목 글자수 위반")
    @Test
    void post_quotations_fail_emptyTitle() throws Exception {
        // given
        Long matchId = 1L;
        QuotationRequest.addQuotation request = new QuotationRequest.addQuotation(
                null,
                1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /quotations : fail, 제목 글자수 위반")
    @Test
    void post_quotations_fail_titleSizeZero() throws Exception {
        // given
        Long matchId = 1L;
        QuotationRequest.addQuotation request = new QuotationRequest.addQuotation(
                "",
                1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /quotations : fail, 견적가격 음수")
    @Test
    void post_quotations_fail_negativePrice() throws Exception {
        // given
        Long matchId = 1L;
        QuotationRequest.addQuotation request = new QuotationRequest.addQuotation(
                "my wedding",
                -1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /quotations : fail, 견적가격 누락")
    @Test
    void post_quotations_fail_emptyPrice() throws Exception {
        // given
        Long matchId = 1L;
        QuotationRequest.addQuotation request = new QuotationRequest.addQuotation(
                "my wedding",
                null,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("GET /quotations : success")
    @Test
    void get_quotations_success() throws Exception {
        // given
        Long matchId = 1L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response.status").value("완료"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].title").value("test"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].price").value("1000000"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].company").value("abc"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].description").value("asdf"));
        resultActions.andExpect(jsonPath("$.response.quotations[0].status").value("완료"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].title").value("test2"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].price").value("1000000"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].company").value("abc2"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].description").value("asdf2"));
        resultActions.andExpect(jsonPath("$.response.quotations[1].status").value("완료"));
    }

    @DisplayName("GET /quotations : fail, 음수 matchId")
    @Test
    void get_quotations_fail_negativeMatchId() throws Exception {
        // given
        Long matchId = -1L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/quotations")
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /quotations/confirm/{quotationId}?matchId={matchId} : success")
    @Test
    void quotationsConfirm_success() throws Exception {
        // given
        Long matchId = 2L;
        Long quotationId = 3L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations/confirm/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("matchId", String.valueOf(matchId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }
}