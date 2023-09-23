package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;
    private static final int PAGE_SIZE = 10;

    @DeleteMapping("/portfolios")
    public ResponseEntity<?> deletePortfolio() {

        // TODO: Security로 전달받은 플래너의 포트폴리오 삭제

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
