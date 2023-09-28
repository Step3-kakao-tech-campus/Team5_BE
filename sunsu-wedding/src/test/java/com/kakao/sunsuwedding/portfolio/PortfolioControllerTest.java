package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.security.SecurityConfig;
import com.kakao.sunsuwedding.user.UserRequest;
import com.kakao.sunsuwedding.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Import({
        SecurityConfig.class
})
@Sql("classpath:/db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class PortfolioControllerTest {
    private final MockMvc mockMvc;
    private final UserService userService;
    private String plannerToken;
    private String coupleToken;

    public PortfolioControllerTest(@Autowired MockMvc mockMvc,
                                   @Autowired UserService userService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
    }

    @BeforeEach
    void setUp() {
        UserRequest.LoginDTO plannerLoginRequest = new UserRequest.LoginDTO();
        plannerLoginRequest.setRole("planner");
        plannerLoginRequest.setEmail("planner@gmail.com");
        plannerLoginRequest.setPassword("planner1234!");
        plannerToken = userService.login(plannerLoginRequest);

        UserRequest.LoginDTO coupleLoginRequest = new UserRequest.LoginDTO();
        coupleLoginRequest.setRole("couple");
        coupleLoginRequest.setEmail("couple@gmail.com");
        coupleLoginRequest.setPassword("couple1234!");
        coupleToken = userService.login(coupleLoginRequest);
    }

    @DisplayName("GET /portfolios : success")
    @Test
    void getPortfolios_success() throws Exception{
        // given
        Long page = 0L;

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/portfolios")
                        .param("page", String.valueOf(page))
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].title").value("test1"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].plannerName").value("planner"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].price").value("1000000"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].location").value("부산"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[0].contractCount").value("10"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[1].title").value("test2"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[1].plannerName").value("planner2"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[1].price").value("2000000"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[1].location").value("부산"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response[1].contractCount").value("20"));
    }

    @DisplayName("GET /portoflios : fail, 음수 페이지 요청")
    @Test
    void getPortfolios_fail_negativePageRequest() throws Exception {
        // given
        Long page = -1L;

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/portfolios")
                        .param("page", String.valueOf(page))
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
    }

    @DisplayName("GET /portfolios/{id} : success")
    @Test
    void getPortfolioById_success() throws Exception{
        // given
        Long portfolioId = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/portfolios/" + portfolioId)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response.title").value("test1"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response.description").value("test1"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response.location").value("부산"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response.plannerName").value("planner"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response.contractCount").value("10"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response.career").value("none"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response.partnerCompany").value("none"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.response.priceInfo.totalPrice").value("1000000"));
    }

    @DisplayName("GET /portfolios/{id} : fail, 존재하지 않는 포트폴리오 조회 요청")
    @Test
    void getPortfolioById_fail_portfolioNotFound() throws Exception{
        // given
        Long portfolioId = 100000L;

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/portfolios/" + portfolioId)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
    }

    @DisplayName("DELETE /portfolios : success")
    @Test
    void deletePortfolio_success() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/portfolios")
                        .header("Authorization", plannerToken)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("DELETE /portfolios : fail, 커플 유저가 포트폴리오 삭제 요청")
    @Test
    void deletePortfolio_fail_permissionDenied() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/portfolios")
                        .header("Authorization", coupleToken)
        );

        // then
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
    }
}
