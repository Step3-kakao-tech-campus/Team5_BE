package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.match.Quotation.QuotationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class MatchRestController {

    private final MatchService matchService;

    // 채팅방 생성
    @PostMapping("")
    public ResponseEntity<?> addChat(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MatchRequest.AddMatchDTO request) {
        matchService.addChat(userDetails.getInfo(), request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // Match Delete : isActive 필드 false
    @DeleteMapping("")
    public ResponseEntity<?> deleteChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long matchId) {
        matchService.deleteChat(userDetails.getInfo(), matchId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}