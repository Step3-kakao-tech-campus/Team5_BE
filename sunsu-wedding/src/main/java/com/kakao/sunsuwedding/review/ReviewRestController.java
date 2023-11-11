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
@RequestMapping("/api/review")
public class ReviewRestController {
    private final ReviewServiceImpl reviewServiceImpl;
    @PostMapping("")
    public ResponseEntity<?> addReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestParam @Min(1) Long chatId,
                                       @Valid @RequestBody ReviewRequest.AddDTO request) {
        reviewServiceImpl.addReview(userDetails.getUser(), chatId, request);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 특정 플래너에게 작성된 리뷰를 모아봄
    @GetMapping("")
    public ResponseEntity<?> findReviewsByPlanner(@RequestParam(defaultValue = "0") @Min(0) Integer page, @RequestParam Long plannerId) {
        ReviewResponse.FindAllByPlannerDTO response = reviewServiceImpl.findReviewsByPlanner(page, plannerId);

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 예비 부부가 자신이 작성한 리뷰를 모두 모아봄
    @GetMapping("/all")
    public  ResponseEntity<?> findReviewsByCouple(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ReviewResponse.FindAllByCoupleDTO response = reviewServiceImpl.findReviewsByCouple(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findReviewById(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long reviewId) {
        ReviewResponse.ReviewDTO response = reviewServiceImpl.findReviewById(userDetails.getUser(), reviewId);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable @Min(1) Long reviewId,
                                          @Valid @RequestBody ReviewRequest.UpdateDTO request) {
        reviewServiceImpl.updateReview(userDetails.getUser(), reviewId, request);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable @Min(1) Long reviewId) {
        reviewServiceImpl.deleteReview(userDetails.getUser(), reviewId);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
