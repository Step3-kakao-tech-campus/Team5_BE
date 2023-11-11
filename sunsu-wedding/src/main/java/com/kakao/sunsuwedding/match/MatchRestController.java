package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match")
public class MatchRestController {

    private final MatchServiceImpl matchServiceImpl;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                     @RequestParam @Min(1) Long chatId) {
        matchServiceImpl.confirm(userDetails.getUser(), chatId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/review")
    public ResponseEntity<?> findMatchesWithNoReview(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MatchResponse.FindAllWithNoReviewDTO response = matchServiceImpl.findMatchesWithNoReview(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

}