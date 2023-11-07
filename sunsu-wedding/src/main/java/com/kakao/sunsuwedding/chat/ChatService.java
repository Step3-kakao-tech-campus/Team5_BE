package com.kakao.sunsuwedding.chat;

import com.kakao.sunsuwedding.user.base_user.User;

public interface ChatService {

    ChatResponse.ChatDTO addChat(User user, ChatRequest.AddChatDTO requestDTO);
}
