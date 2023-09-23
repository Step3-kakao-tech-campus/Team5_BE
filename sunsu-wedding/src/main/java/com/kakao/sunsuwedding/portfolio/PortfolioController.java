package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioListItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;
    private static final int PAGE_SIZE = 10;

    @GetMapping("/portfolios")
    public ResponseEntity<?> getPortfolios(@RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        List<PortfolioListItemDTO> items = portfolioService.getPortfolios(pageRequest);
        return ResponseEntity.ok().body(ApiUtils.success(items));
    }

    @GetMapping("/portfolios/{id}")
    public ResponseEntity<?> getPortfolioInDetail(@PathVariable Long id) {
        PortfolioDTO portfolio = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok().body(ApiUtils.success(portfolio));
    }
}
