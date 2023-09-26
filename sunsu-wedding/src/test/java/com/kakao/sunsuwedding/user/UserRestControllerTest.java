package com.kakao.sunsuwedding.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

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

        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

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

        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

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

        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : "+responseBody);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("role은 플래너, 또는 예비 부부만 가능합니다."));
    }
}
