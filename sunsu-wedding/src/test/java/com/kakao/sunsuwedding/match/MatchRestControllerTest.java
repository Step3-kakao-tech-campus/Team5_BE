package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding._core.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@TestPropertySource(properties = {
        "security.jwt-config.secret.access=your-test-access-secret",
        "security.jwt-config.secret.refresh=your-test-refresh-secret",
        "payment.toss.secret=your-test-toss-payment-secret",
        "email.username=test@email.com",
        "email.password=qweasdzxc"
})
@AutoConfigureMockMvc
@SpringBootTest
public class MatchRestControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(MatchRestControllerTest.class);

    @Autowired
    private MockMvc mvc;

    // ============ 견적서 전체(매칭) 확정 테스트 ============
    @DisplayName("견적서 전체 확정 성공 테스트")
    @Test
    @WithUserDetails("couple4@gmail.com")
    public void match_confirm_all_success_test() throws Exception {
        //given
        Long chatId = 7L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/match/confirm?chatId=" + chatId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("견적서 전체 확정 실패 테스트 1 - 일부 견적서 미확정 시")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_confirm_all_fail_test1() throws Exception {
        //given
        Long chatId = 2L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/match/confirm?chatId=" + chatId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("확정되지 않은 견적서가 있습니다."));
    }

    @DisplayName("견적서 전체 확정 실패 테스트 2 - 견적서 없을 시")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_confirm_all_fail_test2() throws Exception {
        //given
        Long chatId = 4L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/match/confirm?chatId=" + chatId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("확정할 견적서가 없습니다."));
    }

    @DisplayName("견적서 전체 확정 실패 테스트 3 - 존재하지 않는 매칭 내역")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_confirm_all_fail_test3() throws Exception {
        //given
        Long chatId = 8L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/match/confirm?chatId=" + chatId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("매칭 내역을 찾을 수 없습니다."));
    }

    @DisplayName("견적서 전체 확정 실패 테스트 4 - 본인의 매칭 내역이 아님")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_confirm_all_fail_test4() throws Exception {
        //given
        Long chatId = 3L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/match/confirm?chatId=" + chatId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(403));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("사용할 수 없는 기능입니다."));
    }

    // ============ 리뷰 작성 가능한 매칭 조회 테스트 ============
    @DisplayName("리뷰 작성 가능한 매칭 조회 성공 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void find_match_with_no_review_success_test() throws Exception {
        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/match/review")
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.matches[0].chatId").value(6));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.matches[0].plannerId").value(2));
    }


    private void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }
}
