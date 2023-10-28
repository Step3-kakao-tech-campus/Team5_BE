package com.kakao.sunsuwedding.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.security.SecurityConfig;
import com.kakao.sunsuwedding.user.UserRestControllerTest;
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

@Import({
        SecurityConfig.class,
})
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
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
    @WithUserDetails("planner@gmail.com")
    void save_payment_success() throws Exception {
        // given
        PaymentRequest.SaveDTO requestDTO = new PaymentRequest.SaveDTO();
        requestDTO.setAmount(1000L);
        requestDTO.setOrderId("orderId1");
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
        PaymentRequest.SaveDTO requestDTO = new PaymentRequest.SaveDTO();
        requestDTO.setAmount(1000L);
        requestDTO.setOrderId("");
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

    // ============ 결제 정보 검증 테스트 ============
    @DisplayName("결제 정보 검증 성공 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    void confirm_payment_success() throws Exception {
        // when
        PaymentRequest.ConfirmDTO requestDTO = new PaymentRequest.ConfirmDTO();
        requestDTO.setAmount(1000L);
        requestDTO.setPaymentKey("payment");
        requestDTO.setOrderId("order");
        String requestBody = om.writeValueAsString(requestDTO);

        //given
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/confirm")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("결제 정보 검증 실패 테스트 - 잘못된 amount")
    @Test
    @WithUserDetails("couple@gmail.com")
    void confirm_payment_fail() throws Exception {
        // when
        PaymentRequest.ConfirmDTO requestDTO = new PaymentRequest.ConfirmDTO();
        requestDTO.setAmount(10000L);
        requestDTO.setPaymentKey("payment");
        requestDTO.setOrderId("order");
        String requestBody = om.writeValueAsString(requestDTO);

        //given
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/confirm")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response").value("fail"));
    }

    // ============ 유저 등급 업그레이드 테스트 ============
    @DisplayName("유저 등급 업그레이드 성공 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    void user_upgrade_success() throws Exception {
        // given
        PaymentRequest.UpgradeDTO requestDTO = new PaymentRequest.UpgradeDTO();
        requestDTO.setAmount(1000L);
        requestDTO.setOrderId("order");
        requestDTO.setPaymentKey("payment");
        requestDTO.setStatus("DONE");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/upgrade")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));

    }
    @DisplayName("유저 등급 업그레이드 실패 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    void user_upgrade_fail() throws Exception {
        // given
        PaymentRequest.UpgradeDTO requestDTO = new PaymentRequest.UpgradeDTO();
        requestDTO.setAmount(1000L);
        requestDTO.setOrderId("order");
        requestDTO.setPaymentKey("payment");
        requestDTO.setStatus("EXPIRED");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/payments/upgrade")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("잘못된 결제 정보입니다."));
    }

    private void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }
}
