package com.kakao.sunsuwedding.favorite;

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
        "payment.toss.secret=your-test-toss-payment-secret",
        "email.username=test@email.com",
        "email.password=qweasdzxc"
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class FavoriteRestControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteRestControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    // ============ 찜하기 추가 테스트 ============
    @DisplayName("찜하기 추가 성공 테스트")
    @Test
    @WithUserDetails("planner0@gmail.com")
    void save_favorite_success() throws Exception {
        // given
        Long portfolioId = 5L;

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/favorite/" + portfolioId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("찜하기 추가 실패 테스트 - 이미 존재하는 찜하기")
    @Test
    @WithUserDetails("planner0@gmail.com")
    void save_favorite_fail() throws Exception {
        // given
        Long portfolioId = 1L;

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/favorite/" + portfolioId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("이미 존재하는 찜하기 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value("400"));
    }

    // ============ 찜하기 취소 테스트 ============
    @DisplayName("찜하기 취소 성공 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    void delete_favorite_success() throws Exception {
        // given
        Long portfolioId = 1L;

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/favorite/" + portfolioId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("찜하기 취소 실패 테스트 - 존재하지 않는 찜하기")
    @Test
    @WithUserDetails("couple@gmail.com")
    void delete_favorite_fail() throws Exception {
        // given
        Long portfolioId = 8L;

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/favorite/" + portfolioId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("존재하지 않는 찜하기 입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value("404"));
    }

    // ============ 찜하기 모아보기 테스트 ============
    @DisplayName("찜하기 모아보기 성공 테스트")
    @Test
    @WithUserDetails("planner0@gmail.com")
    void find_all_favorite_success() throws Exception {
        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].id").value(2L));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].title").value("test2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].plannerName").value("planner2"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].price").value("2000000"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].location").value("부산"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].contractCount").value("20"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].isLiked").value(true));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[1].id").value(1L));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response[1].isLiked").value(true));
    }

    private void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }
}
