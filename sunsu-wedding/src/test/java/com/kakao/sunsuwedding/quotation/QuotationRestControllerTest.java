package com.kakao.sunsuwedding.quotation;

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
public class QuotationRestControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(UserRestControllerTest.class);

    @Autowired
    private MockMvc mvc;


    @DisplayName("전체 확정 업데이트 성공 테스트")
    @Test
    @WithUserDetails("couple@gmail.com")
    public void match_confirm_all_success_test() throws Exception {
        //given
        Long matchId = 1L;

        //when
        ResultActions result = mvc.perform(
                MockMvcRequestBuilders
                        .post("/quotations/confirmAll/1")
        );

        String responseBody = result.andReturn().getResponse().getContentAsString();
        logger.debug("테스트 : " + responseBody);
        System.out.println("테스트 : " + responseBody);

        // then
        result.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"));
    }
}