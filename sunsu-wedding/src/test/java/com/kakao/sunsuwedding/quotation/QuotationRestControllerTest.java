package com.kakao.sunsuwedding.quotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding._core.config.SecurityConfig;
import com.kakao.sunsuwedding.user.UserRequest;
import com.kakao.sunsuwedding.user.UserResponse;
import com.kakao.sunsuwedding.user.UserServiceImpl;
import com.kakao.sunsuwedding.user.token.TokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Import({
        SecurityConfig.class,
})
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "security.jwt-config.secret.access=your-test-access-secret",
        "security.jwt-config.secret.refresh=your-test-refresh-secret",
        "payment.toss.secret=your-test-toss-payment-secret",
        "email.username=test@email.com",
        "email.password=qweasdzxc"
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class QuotationRestControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(QuotationRestControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private JWTProvider jwtProvider;

    private String plannerToken;

    @BeforeEach
    void beforeEach() {
        UserRequest.LoginDTO request = new UserRequest.LoginDTO("planner1@gmail.com", "planner1234!");
        Pair<TokenDTO, UserResponse.FindUserId> response = userServiceImpl.login(request);
        plannerToken = response.getFirst().accessToken();
    }
    // ============ 견적서 등록 테스트 ============
    @DisplayName("POST /api/quotation : success")
    @Test
    void post_quotations_success() throws Exception {
        // given
        Long chatId = 6L;
        QuotationRequest.Add request = new QuotationRequest.Add(
                "my wedding",
                1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation")
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /api/quotation : fail, 제목 글자수 위반")
    @Test
    void post_quotations_fail_titleTextSize() throws Exception {
        // given
        Long chatId = 1L;
        QuotationRequest.Add request = new QuotationRequest.Add(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbb",
                1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation")
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /api/quotation : fail, 제목 글자수 위반")
    @Test
    void post_quotation_fail_emptyTitle() throws Exception {
        // given
        Long chatId = 1L;
        QuotationRequest.Add request = new QuotationRequest.Add(
                null,
                1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation")
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /api/quotation : fail, 제목 글자수 위반")
    @Test
    void post_quotation_fail_titleSizeZero() throws Exception {
        // given
        Long chatId = 1L;
        QuotationRequest.Add request = new QuotationRequest.Add(
                "",
                1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation")
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /api/quotation : fail, 견적가격 음수")
    @Test
    void post_quotation_fail_negativePrice() throws Exception {
        // given
        Long chatId = 1L;
        QuotationRequest.Add request = new QuotationRequest.Add(
                "my wedding",
                -1500000L,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation")
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /api/quotation : fail, 견적가격 누락")
    @Test
    void post_quotation_fail_emptyPrice() throws Exception {
        // given
        Long chatId = 1L;
        QuotationRequest.Add request = new QuotationRequest.Add(
                "my wedding",
                null,
                "abc studio",
                "very good"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation")
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    // ============ 견적서 조회 테스트 ============
    @DisplayName("GET /api/quotation : success")
    @Test
    void get_quotation_success() throws Exception {
        // given
        Long chatId = 6L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/quotation")
                        .header(jwtProvider.AUTHORIZATION_HEADER, plannerToken)
                        .param("chatId", String.valueOf(chatId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response.status").value("완료"));
        resultActions.andExpect(jsonPath("$.response.totalPrice").value(1000000));
        resultActions.andExpect(jsonPath("$.response.confirmedPrice").value(1000000));
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

    @DisplayName("GET /api/quotation : fail, 음수 chatId")
    @Test
    void get_quotation_fail_negativechatId() throws Exception {
        // given
        Long chatId = -1L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/quotation")
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }



    // ============ 견적서 모아보기 테스트 ============
    @DisplayName("견적서 모아보기 성공 테스트")
    @Test
    void find_by_user_success_test() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/quotation/all")
                        .header("Authorization", plannerToken)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response.chats[0].chatId").value(7));
        resultActions.andExpect(jsonPath("$.response.chats[0].partnerName").value("couple4"));
        resultActions.andExpect(jsonPath("$.response.chats[0].quotations[0].id").value(7));
        resultActions.andExpect(jsonPath("$.response.chats[0].quotations[0].price").value(1000000));
    }



    // ============ 견적서 1개 확정 테스트 ============
    @DisplayName("POST /api/quotation/confirm/{quotationId}?chatId={chatId} : success")
    @Test
    void post_quotationConfirm_success() throws Exception {
        // given
        Long chatId = 2L;
        Long quotationId = 3L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation/confirm/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("POST /api/quotation/confirm/{quotationId}?chatId={chatId} : fail, 음수 id 요청")
    @Test
    void post_quotationConfirm_fail_negativeId() throws Exception {
        // given
        Long chatId = -2L;
        Long quotationId = -3L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation/confirm/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /api/quotation/confirm/{quotationId}?chatId={chatId} : fail, 이미 확정된 견적서를 다시 확정 요청")
    @Test
    void post_quotationConfirm_fail_alreadyConfirmed() throws Exception {
        // given
        Long chatId = 5L;
        Long quotationId = 6L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation/confirm/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /api/quotation/confirm/{quotationId}?chatId={chatId} : fail, 존재하지 않는 견적서 확정 요청")
    @Test
    void post_quotationConfirm_fail_quotationNotExist() throws Exception {
        // given
        Long chatId = 1L;
        Long quotationId = 100L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation/confirm/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("POST /api/quotation/confirm/{quotationId}?chatId={chatId} : fail, 다른 플래너의 견적서 확정 요청")
    @Test
    void post_quotationConfirm_fail_permissionDenied() throws Exception {
        // given
        Long chatId = 1L;
        Long quotationId = 100L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/quotation/confirm/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }



    // ============ 견적서 수정 테스트 ============
    @DisplayName("PUT /api/quotation/{quotationId}?chatId={chatId} : success")
    @Test
    void put_quotationUpdate_success() throws Exception {
        // given
        Long chatId = 2L;
        Long quotationId = 3L;
        QuotationRequest.Update request = new QuotationRequest.Update(
                "updated title",
                500000L,
                "updated company",
                "updated description"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/quotation/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("PUT /api/quotation/{quotationId}?chatId={chatId} : fail, 타이틀 누락")
    @Test
    void put_quotationUpdate_fail_emptyTitle() throws Exception {
        // given
        Long chatId = 2L;
        Long quotationId = 3L;
        QuotationRequest.Update request = new QuotationRequest.Update(
                "",
                500000L,
                "updated company",
                "updated description"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/quotation/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("PUT /api/quotation/{quotationId}?chatId={chatId} : fail, 가격 누락")
    @Test
    void put_quotationUpdate_fail_emptyPrice() throws Exception {
        // given
        Long chatId = 2L;
        Long quotationId = 3L;
        QuotationRequest.Update request = new QuotationRequest.Update(
                "updated title",
                null,
                "updated company",
                "updated description"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/quotation/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("PUT /api/quotation/{quotationId}?chatId={chatId} : fail, 음수 가격으로 변경 요청")
    @Test
    void put_quotationUpdate_fail_negativePrice() throws Exception {
        // given
        Long chatId = 2L;
        Long quotationId = 3L;
        QuotationRequest.Update request = new QuotationRequest.Update(
                "updated title",
                -1000000L,
                "updated company",
                "updated description"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/quotation/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("PUT /api/quotation/{quotationId}?chatId={chatId} : fail, 존재하지 않는 견적서 수정 요청")
    @Test
    void put_quotationUpdate_fail_quotationNotExist() throws Exception {
        // given
        Long chatId = 2L;
        Long quotationId = 100L;
        QuotationRequest.Update request = new QuotationRequest.Update(
                "updated title",
                1000000L,
                "updated company",
                "updated description"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/quotation/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    @DisplayName("PUT /api/quotation/{quotationId}?chatId={chatId} : fail, 다른 플래너의 견적서 수정 요청")
    @Test
    void put_quotationUpdate_fail_permissionDenied() throws Exception {
        // given
        Long chatId = 5L;
        Long quotationId = 6L;
        QuotationRequest.Update request = new QuotationRequest.Update(
                "updated title",
                1000000L,
                "updated company",
                "updated description"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/quotation/" + quotationId)
                        .header("Authorization", plannerToken)
                        .param("chatId", String.valueOf(chatId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
    }

    // ============ 견적서 삭제 테스트 ============
    @DisplayName("견적서 삭제 성공 테스트")
    @Test
    void delete_quotation_success_test() throws Exception {
        // given
        Long quotationId = 3L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/quotation/" + quotationId)
                        .header("Authorization", plannerToken)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    @DisplayName("견적서 삭제 실패 테스트 - 본인의 요청 X")
    @Test
    void delete_quotation_fail_test_permission_denied() throws Exception {
        // given
        Long quotationId = 6L;

        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/quotation/" + quotationId)
                        .header("Authorization", plannerToken)
        );

        // then
        resultActions.andExpect(jsonPath("$.success").value("false"));
        resultActions.andExpect(jsonPath("$.error.message").value("사용할 수 없는 기능입니다."));
        resultActions.andExpect(jsonPath("$.error.status").value("403"));
    }





    private void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }
}