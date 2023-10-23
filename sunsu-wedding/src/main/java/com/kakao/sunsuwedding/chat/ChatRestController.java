package com.kakao.sunsuwedding.chat;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRestController {
    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("")
    public ResponseEntity<?> addChat(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @Valid @RequestBody ChatRequest.AddChatDTO request) {
        ChatResponse.ChatDTO response = chatService.addChat(userDetails.getInfo(), request);

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 채팅방 삭제...? X
    // Match Delete : isActive 필드 false
    /*
    @DeleteMapping("")
    public ResponseEntity<?> deleteChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long matchId) {
        chatService.deleteChat(userDetails.getInfo(), matchId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
     */



}
