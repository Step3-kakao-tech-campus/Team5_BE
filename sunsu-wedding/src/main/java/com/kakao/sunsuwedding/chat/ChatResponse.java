package com.kakao.sunsuwedding.chat;

public class ChatResponse {
    public record ChatDTO(
            Boolean existed,
            Long chatId
    ) {}
}
