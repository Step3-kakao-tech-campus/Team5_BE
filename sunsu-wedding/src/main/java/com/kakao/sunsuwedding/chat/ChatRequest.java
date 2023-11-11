package com.kakao.sunsuwedding.chat;


import jakarta.validation.constraints.NotNull;

public class ChatRequest {
    public record AddChatDTO(
            @NotNull(message = "plannerId는 비어있으면 안됩니다.")
            Long plannerId
    ) {}
}
