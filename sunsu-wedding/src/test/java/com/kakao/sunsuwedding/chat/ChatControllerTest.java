package com.kakao.sunsuwedding.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
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


@Import({
        SecurityConfig.class,
})
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ChatControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ChatControllerTest.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    // ============ 채팅방 생성 테스트 ============
    @DisplayName("채팅방 생성 성공 테스트")
    @Test
    @WithUserDetails("couple3@gmail.com")
    public void add_chat_success_test() throws Exception {
        //given
        ChatRequest.AddChatDTO requestDTO = new ChatRequest.AddChatDTO(1L);
        String requestBody = om.writeValueAsString(requestDTO);

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/chat")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.response.chatId").value(8L));
    }

    @DisplayName("채팅방 생성 실패 테스트 1 - 이미 매칭내역(채팅방) 존재")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void add_chat_fail_test_already_exist() throws Exception {
        //given
        ChatRequest.AddChatDTO requestDTO = new ChatRequest.AddChatDTO(2L);
        String requestBody = om.writeValueAsString(requestDTO);

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/chat")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("이미 존재하는 매칭입니다."));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
    }

    @DisplayName("채팅방 생성 실패 테스트 2 - 존재하지 않는 플래너")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void add_chat_fail_test_planner_not_found() throws Exception {
        //given
        ChatRequest.AddChatDTO requestDTO = new ChatRequest.AddChatDTO(100L);
        String requestBody = om.writeValueAsString(requestDTO);

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/chat")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("서비스를 탈퇴하거나 가입하지 않은 플래너입니다"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));
    }

    private void logResult(ResultActions result) throws Exception {
        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
    }

    /*
    // ============ 채팅방 삭제 테스트 ============
    @DisplayName("채팅방 삭제 성공 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_delete_success_test() throws Exception {
        //given
        Long matchId = 1L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/chat?matchId=" + matchId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }

    @DisplayName("채팅방 삭제 실패 테스트 1 - 견적서 전체 확정 되지 않음")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_delete_fail_test1() throws Exception {
        //given
        Long matchId = 2L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/chat?matchId=" + matchId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(400));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("견적서 전체 확정을 해야 합니다."));

    }

    @DisplayName("채팅방 삭제 실패 테스트 2 - 본인의 채팅방이 아님")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_delete_fail_test2() throws Exception {
        //given
        Long matchId = 3L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/chat?matchId=" + matchId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(403));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("사용할 수 없는 기능입니다."));
    }

    @DisplayName("채팅방 삭제 실패 테스트 3 - 존재하지 않는 채팅방(매칭 내역)")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_delete_fail_test3() throws Exception {
        //given
        Long matchId = 10L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .delete("/chat?matchId=" + matchId)
        );

        logResult(result);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(404));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("매칭 내역을 찾을 수 없습니다."));
    }
     */
}
