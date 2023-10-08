package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class MatchRestController {

    private final MatchService matchService;

    // Match Delete : isActive 필드 false
    @DeleteMapping("/{matchId}")
    public ResponseEntity<?> deleteChat(@PathVariable Long matchId) {
        matchService.deleteChat(matchId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}