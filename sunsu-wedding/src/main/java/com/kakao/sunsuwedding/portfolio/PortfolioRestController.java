package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.image.ImageItemService;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class PortfolioRestController {
    private final PortfolioService portfolioService;
    private final ImageItemService imageItemService;

    @PostMapping(value = "/portfolios", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> addPortfolios(@RequestPart PortfolioRequest.addDTO request,
                                           @RequestPart MultipartFile[] images,
                                           Error errors,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pair<Portfolio, Planner> info = portfolioService.addPortfolio(request, userDetails.getPlanner().getId());
        imageItemService.uploadImage(images, info.getFirst(), info.getSecond());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PutMapping(value = "/portfolios", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> updatePortfolios(@RequestPart PortfolioRequest.updateDTO request,
                                           @RequestPart MultipartFile[] images,
                                           Error errors,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pair<Portfolio, Planner> info = portfolioService.updatePortfolio(request, userDetails.getPlanner().getId());

        // TODO: 이미지 업데이트 처리
        imageItemService.updateImage(images, info.getFirst(), info.getSecond());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }




}
