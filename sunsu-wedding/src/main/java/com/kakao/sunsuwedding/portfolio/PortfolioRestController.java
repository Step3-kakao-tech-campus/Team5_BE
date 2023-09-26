package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PortfolioRestController {
    private final PortfolioService portfolioService;

    @PostMapping(value = "/portfolios")
    public ResponseEntity<?> createPortfolios() {
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }






}
