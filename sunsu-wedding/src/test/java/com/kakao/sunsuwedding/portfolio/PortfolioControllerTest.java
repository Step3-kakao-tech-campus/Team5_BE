package com.kakao.sunsuwedding.portfolio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.config.SecurityConfig;
import org.hamcrest.core.IsNull;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Import({
        SecurityConfig.class
})
@ActiveProfiles("test")
@Sql("classpath:/db/teardown.sql")
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "security.jwt-config.secret.access=your-test-access-secret",
        "security.jwt-config.secret.refresh=your-test-refresh-secret",
        "payment.toss.secret=your-test-toss-payment-secret",
        "email.username=test@email.com",
        "email.password=qweasdzxc",
        "email.test-code=999999"
})
@EnableWebMvc
@SpringBootTest
public class PortfolioControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioControllerTest.class);
    private final MockMvc mockMvc;
    private final ObjectMapper om;

    public PortfolioControllerTest(@Autowired MockMvc mockMvc,
                                   @Autowired ObjectMapper om) {
        this.mockMvc = mockMvc;
        this.om = om;
    }

    // ============ 포트폴리오 등록 테스트 ============
    @DisplayName("포트폴리오 등록 성공 테스트")
    @Test
    @WithUserDetails("planner0@gmail.com")
    public void add_portfolio_success_test() throws Exception {
        // given
        String requestBody = om.writeValueAsString(getAddDTO());

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }


    @DisplayName("포트폴리오 등록 실패 테스트 1 - 존재하지 않는 유저")
    @Test
    @WithUserDetails("planner17@gmail.com")
    public void add_portfolio_fail_test_user_not_found() throws Exception {
        // given
        String requestBody = om.writeValueAsString(getAddDTO());

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("서비스를 탈퇴했거나 가입하지 않은 유저의 요청입니다."));
    }

    @DisplayName("포트폴리오 등록 실패 테스트 2 - 이미 포트폴리오 존재")
    @Test
    @WithUserDetails("planner1@gmail.com")
    public void add_portfolio_fail_test_already_exist() throws Exception {
        // given
        String requestBody = om.writeValueAsString(getAddDTO());

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당 플래너의 포트폴리오가 이미 존재합니다. 포트폴리오는 플래너당 하나만 생성할 수 있습니다."));
    }

    @DisplayName("포트폴리오 등록 실패 테스트 3 - 이미지 개수 5개 초과")
    @Test
    @WithUserDetails("planner0@gmail.com")
    public void add_portfolio_fail_test_image_limit_exceed() throws Exception {
        // given
        String requestBody = om.writeValueAsString(exceed_five_getAddDTO());

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("요청한 이미지의 수가 5개를 초과합니다."));
    }

    // ============ 포트폴리오 리스트 조회 테스트 ============
    @DisplayName("포트폴리오 리스트 조회 성공 테스트 1 - 다음 페이지 존재")
    @Test
    public void get_portfolios_success_test_next_page_exist() throws Exception {
        //given
        Long cursor = 13L;

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/portfolio?cursor="+ cursor)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.data[0].id").value(12));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.data[0].plannerName").value("planner12"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.data[0].location").value("부산"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.cursor").value(3));
    }

    @DisplayName("포트폴리오 리스트 조회 성공 테스트 2 - 마지막 페이지")
    @Test
    public void get_portfolios_success_test_last_page() throws Exception {
        //given
        Long nextCursor = 3L;
        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/portfolio?cursor={nextCursor}", nextCursor)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.data[0].id").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.data.length()").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.cursor").value(IsNull.nullValue()));
    }

    // ============ 포트폴리오 상세 조회 테스트 ============
    @DisplayName("포트폴리오 상세 조회 성공 테스트 - 예비부부 (PREMIUM 등급)")
    @Test
    @WithUserDetails("couple2@gmail.com")
    public void get_portfolio_by_id_premium_success_test() throws Exception {
        //given
        Long id = 1L;
        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/portfolio/{id}", id)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.userId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.priceInfo.items[0].itemTitle").value("스튜디오1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.priceInfo.items[0].itemPrice").value(500000));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.paymentsHistory.avgPrice").value(1000000));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.paymentsHistory.payments[0].confirmedAt").value("2023-10"));
    }

    @DisplayName("포트폴리오 상세 조회 성공 테스트 - 플래너 (NORMAL 등급)")
    @Test
    @WithUserDetails("planner1@gmail.com")
    public void get_portfolio_by_id_normal_success_test() throws Exception {
        //given
        Long id = 1L;
        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/portfolio/{id}", id)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.userId").value(2));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.priceInfo.items[0].itemTitle").value("스튜디오1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.priceInfo.items[0].itemPrice").value(500000));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.paymentsHistory").isEmpty());
    }


    @DisplayName("포트폴리오 상세 조회 실패 테스트 1 - 존재하지 않는 포트폴리오")
    @Test
    @WithUserDetails("couple2@gmail.com")
    public void get_portfolio_by_id_fail_test_portfolio_not_found() throws Exception {
        //given
        Long id = 30L;
        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/portfolio/{id}", id)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당하는 플래너의 포트폴리오가 삭제되었거나 존재하지 않습니다."));
    }

    @DisplayName("포트폴리오 상세 조회 실패 테스트 2 - 탈퇴한 플래너")
    @Test
    @WithUserDetails("couple2@gmail.com")
    public void get_portfolio_by_id_fail_test_planner_not_found() throws Exception {
        //given
        Long id = 19L; // 탈퇴한 플래너의 포트폴리오 id
        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/portfolio/{id}", id)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당하는 플래너의 포트폴리오가 삭제되었거나 존재하지 않습니다."));
    }

    // ============ 포트폴리오 수정 테스트 ============
    @DisplayName("포트폴리오 수정 성공 테스트")
    @Test
    @WithUserDetails("planner1@gmail.com")
    public void update_portfolio_success_test() throws Exception {
        // given
        String requestBody = om.writeValueAsString(getUpdateDTO());

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("포트폴리오 수정 실패 테스트 1 - 존재하지 않는 유저")
    @Test
    @WithUserDetails("planner17@gmail.com")
    public void update_portfolio_fail_test_user_not_found() throws Exception {
        // given
        String requestBody = om.writeValueAsString(getUpdateDTO());

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("서비스를 탈퇴했거나 가입하지 않은 유저의 요청입니다."));
    }

    @DisplayName("포트폴리오 수정 실패 테스트 2 - 포트폴리오 존재하지 않음")
    @Test
    @WithUserDetails("planner0@gmail.com")
    public void update_portfolio_fail_test_portfolio_not_found() throws Exception {
        // given
        String requestBody = om.writeValueAsString(getUpdateDTO());

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("해당하는 플래너의 포트폴리오가 삭제되었거나 존재하지 않습니다."));
    }


    @DisplayName("포트폴리오 수정 실패 테스트 3 - 이미지 개수 5개 초과")
    @Test
    @WithUserDetails("planner1@gmail.com")
    public void update_portfolio_fail_test_image_limit_exceed() throws Exception {
        // given
        String requestBody = om.writeValueAsString(exceed_five_getUpdateDTO());

        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/portfolio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("요청한 이미지의 수가 5개를 초과합니다."));
    }



    // ============ 포트폴리오 삭제 테스트 ============
    @DisplayName("포트폴리오 삭제 성공 테스트")
    @Test
    @WithUserDetails("planner18@gmail.com")
    public void delete_portfolio_success_test() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/portfolio")
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("포트폴리오 삭제 실패 테스트 - 플래너가 아님")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void delete_portfolio_fail_test_not_planner() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/portfolio")
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(403));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("사용할 수 없는 기능입니다."));
    }

    private PortfolioRequest.AddDTO getAddDTO() {
        PortfolioRequest.ItemDTO itemDTO = new PortfolioRequest.ItemDTO("헤어", 300000L);
        List<PortfolioRequest.ItemDTO> itemDTOS = List.of(itemDTO);
        List<String> imageItems = List.of("/wAA", "/wAA");
        return new PortfolioRequest.AddDTO("유희정", "title", "description", "부산", "career", "partnerCompany", itemDTOS, imageItems);
    }

    private PortfolioRequest.AddDTO exceed_five_getAddDTO() {
        PortfolioRequest.ItemDTO itemDTO = new PortfolioRequest.ItemDTO("헤어", 300000L);
        List<PortfolioRequest.ItemDTO> itemDTOS = List.of(itemDTO);
        List<String> imageItems = List.of("/wAA", "/wAA", "/wAA", "/wAA", "/wAA", "/wAA");
        return new PortfolioRequest.AddDTO("유희정", "title", "description", "부산", "career", "partnerCompany", itemDTOS, imageItems);
    }

    private PortfolioRequest.UpdateDTO getUpdateDTO() {
        PortfolioRequest.ItemDTO itemDTO = new PortfolioRequest.ItemDTO("드레스", 400000L);
        List<PortfolioRequest.ItemDTO> itemDTOS = List.of(itemDTO);
        List<String> imageItems = List.of("/wAA", "/wAA");
        return new PortfolioRequest.UpdateDTO("김희정", "title2", "description2", "부산", "career2", "partnerCompany2", itemDTOS, imageItems);
    }

    private PortfolioRequest.UpdateDTO exceed_five_getUpdateDTO() {
        PortfolioRequest.ItemDTO itemDTO = new PortfolioRequest.ItemDTO("드레스", 400000L);
        List<PortfolioRequest.ItemDTO> itemDTOS = List.of(itemDTO);
        List<String> imageItems = List.of("/wAA", "/wAA", "/wAA", "/wAA", "/wAA", "/wAA");
        return new PortfolioRequest.UpdateDTO("김희정", "title2", "description2", "부산", "career2", "partnerCompany2", itemDTOS, imageItems);
    }

    private void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }
}