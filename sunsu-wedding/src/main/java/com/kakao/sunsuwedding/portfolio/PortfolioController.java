package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioInsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

}
