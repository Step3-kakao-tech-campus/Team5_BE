package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioInsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioListItemDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioUpdateRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;
    private static final int PAGE_SIZE = 10;

    @PostMapping(value = "/portfolios",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createPortfolios(@RequestPart PortfolioInsertRequest request,
                                              @RequestPart MultipartFile[] images) {

        // TODO: Security를 통해 전달된 플래너 정보를 이용해서 포트폴리오 데이터베이스에 저장
        // TODO: ImageItemService를 통해서 데이터베이스에 이미지 정보 저장

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

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

    @PutMapping("/portfolios")
    public ResponseEntity<?> updatePortfolio(@RequestBody PortfolioUpdateRequest request) {

        // TODO: Security를 통해 전달된 플래너 정보를 이용해서 포트폴리오 업데이트 진행

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("/portfolios")
    public ResponseEntity<?> deletePortfolio() {

        // TODO: Security로 전달받은 플래너의 포트폴리오 삭제

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
