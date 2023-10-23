package com.kakao.sunsuwedding.chat;

import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ChatRequest {
    public record AddChatDTO(
            Long plannerId
    ) {}
}
