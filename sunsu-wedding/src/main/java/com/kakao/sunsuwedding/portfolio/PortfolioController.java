package com.kakao.sunsuwedding.portfolio;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;
    private static final int PAGE_SIZE = 10;
}
