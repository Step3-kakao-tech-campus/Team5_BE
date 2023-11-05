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
@RequestMapping("/match")
public class MatchRestController {

    private final MatchService matchService;

    @PostMapping("/confirmAll")
    public ResponseEntity<?> confirmAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam @Min(1) Long chatId) {
        matchService.confirmAll(userDetails.getUser(), chatId);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> findAllWithNoReview(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MatchResponse.FindAllWithNoReviewDTO response = matchService.findAllWithNoReview(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

}