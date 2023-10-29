package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewRestController {
    private final ReviewService reviewService;
    @PostMapping("")
    public ResponseEntity<?> addReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestParam @Min(1) Long chatId,
                                       @Valid @RequestBody ReviewRequest.AddDTO request) {
        reviewService.addReview(userDetails.getInfo(), chatId, request);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/collect")
    public  ResponseEntity<?> findAllByUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ReviewResponse.FindAllByUserDTO response = reviewService.findAllByUser(userDetails.getInfo());

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("")
    public ResponseEntity<?> findAllByChatId(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestParam @Min(1) Long chatId) {
        ReviewResponse.FindAllByChatIdDTO response = reviewService.findAllByChatId(chatId);

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable @Min(1) Long reviewId,
                                          @Valid @RequestBody ReviewRequest.UpdateDTO request) {
        reviewService.updateReview(userDetails.getInfo(), reviewId, request);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable @Min(1) Long reviewId) {
        reviewService.deleteReview(userDetails.getInfo(), reviewId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
