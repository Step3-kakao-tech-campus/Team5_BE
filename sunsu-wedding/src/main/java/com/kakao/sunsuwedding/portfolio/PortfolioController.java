package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;
    private static final int PAGE_SIZE = 10;

    @PutMapping("/portfolios")
    public ResponseEntity<?> updatePortfolio(@RequestBody PortfolioUpdateRequest request) {

        // TODO: Security를 통해 전달된 플래너 정보를 이용해서 포트폴리오 업데이트 진행

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
