package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class MatchResponse {

    @Getter
    @Setter
    public static class ChatByIdDTO {
        Long chatId;

        public ChatByIdDTO(Match match){
            this.chatId = match.getId();
        }
    }
}
