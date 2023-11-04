package com.kakao.sunsuwedding.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.config.SecurityConfig;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Import({
        SecurityConfig.class,
})
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "security.jwt-config.secret.access=your-test-access-secret",
        "security.jwt-config.secret.refresh=your-test-refresh-secret",
        "payment.toss.secret=your-test-toss-payment-secret"
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class PaymentRestControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(PaymentRestControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    // ============ 결제 데이터 저장 테스트 ============
    @DisplayName("결제 데이터 저장 성공 테스트")
    @Test
    @WithUserDetails("planner0@gmail.com")
    void save_payment_success() throws Exception {
        // given
        PaymentRequest.SaveDTO requestDTO = new PaymentRequest.SaveDTO("order", 1000L);
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/save")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("결제 데이터 저장 실패 테스트 - orderId가 비어있음")
    @Test
    @WithUserDetails("couple@gmail.com")
    void save_payment_fail() throws Exception {
        // given
        PaymentRequest.SaveDTO requestDTO = new PaymentRequest.SaveDTO("",1000L);
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/save")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value("400"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("orderId는 비어있으면 안됩니다."));
    }

    // ============ 유저 통합 승인 테스트 ============
    // 토스 페이먼츠와 연동되어있으므로 독립적인 성공 테스트가 불가능함..
    @DisplayName("유저 통합 승인 실패 - 잘못된 결제 정보")
    @Test
    @WithUserDetails("couple@gmail.com")
    void approve_payment_success() throws Exception {
        // given
        PaymentRequest.ApproveDTO requestDTO = new PaymentRequest.ApproveDTO("098", "payment", 1000L);
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/approve")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("잘못된 결제 정보입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value("400"));
    }



    private void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }
}
