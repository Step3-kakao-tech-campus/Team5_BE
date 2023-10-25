package com.kakao.sunsuwedding.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sunsuwedding._core.security.CustomUserDetailsService;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding._core.security.SecurityConfig;
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
public class UserRestControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserRestControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JWTProvider jwtProvider;

    // ============ 회원가입 테스트 ============
    @DisplayName("회원가입 성공 테스트")
    @Test
    public void user_join_success_test() throws Exception {
        // given
        UserRequest.SignUpDTO requestDTO = new UserRequest.SignUpDTO();
        requestDTO.setEmail("ssarmango@nate.com");
        requestDTO.setRole("couple");
        requestDTO.setPassword("meta1234!");
        requestDTO.setPassword2("meta1234!");
        requestDTO.setUsername("qwer");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/user/signup")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));

    }

    @DisplayName("회원가입 실패 테스트 - 잘못된 이메일 형식")
    @Test
    public void user_join_fail_wrong_email_test() throws Exception {
        // given
        UserRequest.SignUpDTO requestDTO = new UserRequest.SignUpDTO();
        requestDTO.setEmail("asdfqwer");
        requestDTO.setRole("couple");
        requestDTO.setPassword("meta1234!");
        requestDTO.setPassword2("meta1234!");
        requestDTO.setUsername("qwe");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/user/signup")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("이메일 형식으로 작성해주세요"));
    }
    @DisplayName("회원가입 실패 테스트 - 잘못된 role")
    @Test
    public void user_join_fail_wrong_role_test() throws Exception {
        // given
        UserRequest.SignUpDTO requestDTO = new UserRequest.SignUpDTO();
        requestDTO.setEmail("asdf@naver.com");
        requestDTO.setRole("asdf");
        requestDTO.setPassword("meta1234!");
        requestDTO.setPassword2("meta1234!");
        requestDTO.setUsername("asdf");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/user/signup")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("role은 플래너, 또는 예비 부부만 가능합니다."));
    }

    // ============ 로그인 테스트 ============
    @DisplayName("로그인 성공 테스트")
    @Test
    public void user_login_success_test() throws Exception {
        // given
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail("planner@gmail.com");
        requestDTO.setPassword("planner1234!");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/user/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        String responseBody = result.andReturn().getResponse().getContentAsString();
        String accessTokenHeader = result.andReturn().getResponse().getHeader(jwtProvider.AUTHORIZATION_HEADER);
        String refreshTokenHeader = result.andReturn().getResponse().getHeader(jwtProvider.REFRESH_HEADER);
        logger.debug("테스트 : " + responseBody);
        logger.debug("테스트 access token  : " + accessTokenHeader);
        logger.debug("테스트 refresh token : " + refreshTokenHeader);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("로그인 실패 테스트 1 - 존재하지 않는 이메일")
    @Test
    public void user_login_fail_email_not_found_test() throws Exception {
        // given
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail("ssar@nate.com");
        requestDTO.setPassword("meta1234!");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/user/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(jsonPath("$.error.message").value("이메일을 찾을 수 없습니다 : ssar@nate.com"));
    }

    @DisplayName("로그인 실패 테스트 2 - 패스워드 잘못 입력")
    @Test
    public void user_login_fail_wrong_password_test() throws Exception {
        // given
        UserRequest.LoginDTO requestDTO = new UserRequest.LoginDTO();
        requestDTO.setEmail("planner@gmail.com");
        requestDTO.setPassword("meta1234!");
        String requestBody = om.writeValueAsString(requestDTO);

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/user/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(jsonPath("$.success").value("false"));
        result.andExpect(jsonPath("$.error.status").value(400));
        result.andExpect(jsonPath("$.error.message").value("패스워드를 잘못 입력하셨습니다"));
    }

    // ============ 회원 탈퇴 테스트 ============

    @DisplayName("회원 탈퇴 성공 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void user_withdraw_success_test() throws Exception {
        // given

        // when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders.delete("/user")
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    // ============ 유저 정보 조회 ============
    @DisplayName("유저 정보 조회 성공")
    @Test
    @WithUserDetails("planner@gmail.com")
    void get_user_info_success_test() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .get("/user/info")
        );
        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
        resultActions.andExpect(jsonPath("$.response.username").value("planner"));
        resultActions.andExpect(jsonPath("$.response.email").value("planner@gmail.com"));
        resultActions.andExpect(jsonPath("$.response.role").value("planner"));
        resultActions.andExpect(jsonPath("$.response.grade").value("normal"));
    }
    // ============ 유저 토큰 갱신 ============
    @DisplayName("유저 토큰 refresh 성공")
    @Test
    @WithUserDetails("planner@gmail.com")
    void user_token_refresh_success_test() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(
                MockMvcRequestBuilders
                        .put("/user/token")
        );
        // then
        resultActions.andExpect(jsonPath("$.success").value("true"));
    }

    private void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }
}
