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
@RequestMapping("/api/reviews")
public class ReviewRestController {
    private final ReviewService reviewService;
    @PostMapping("")
    public ResponseEntity<?> addReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestParam @Min(1) Long chatId,
                                       @Valid @RequestBody ReviewRequest.AddDTO request) {
        reviewService.addReview(userDetails.getUser(), chatId, request);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("")
    public ResponseEntity<?> findReviewsByPlanner(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                              @Valid @RequestBody ReviewRequest.FindAllByPlannerDTO request) {
        ReviewResponse.FindAllByPlannerDTO response = reviewService.findReviewsByPlanner(page, request.plannerId());

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/collect")
    public  ResponseEntity<?> findReviewsByCouple(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ReviewResponse.FindAllByCoupleDTO response = reviewService.findReviewsByCouple(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findReviewById(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long reviewId) {
        ReviewResponse.ReviewDTO response = reviewService.findReviewById(userDetails.getUser(), reviewId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable @Min(1) Long reviewId,
                                          @Valid @RequestBody ReviewRequest.UpdateDTO request) {
        reviewService.updateReview(userDetails.getUser(), reviewId, request);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable @Min(1) Long reviewId) {
        reviewService.deleteReview(userDetails.getUser(), reviewId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
