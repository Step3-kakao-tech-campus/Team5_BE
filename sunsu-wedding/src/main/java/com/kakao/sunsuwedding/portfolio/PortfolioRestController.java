package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.portfolio.cursor.PageCursor;
import com.kakao.sunsuwedding.portfolio.image.ImageItemService;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PortfolioRestController {
    private final PortfolioService portfolioService;
    private final ImageItemService imageItemService;
    private static final int PAGE_SIZE = 10;

    @PostMapping(value = "/portfolios", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> addPortfolios(@RequestPart PortfolioRequest.addDTO request,
                                           @RequestPart MultipartFile[] images,
                                           Error errors,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pair<Portfolio, Planner> info = portfolioService.addPortfolio(request, userDetails.getUser().getId());
        imageItemService.uploadImage(images, info.getFirst(), info.getSecond());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping(value = "/portfolios")
    public ResponseEntity<?> getPortfolios(@RequestParam @Min(-2) Long cursor) {
        CursorRequest cursorRequest = new CursorRequest(cursor, PAGE_SIZE);
        PageCursor<List<PortfolioResponse.FindAllDTO>> response = portfolioService.getPortfolios(cursorRequest);

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/portfolios/{id}")
    public ResponseEntity<?> getPortfolioInDetail(@PathVariable @Min(1) Long id) {
        PortfolioResponse.FindByIdDTO portfolio = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok().body(ApiUtils.success(portfolio));
    }

    @PutMapping(value = "/portfolios", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> updatePortfolios(@RequestPart PortfolioRequest.updateDTO request,
                                           @RequestPart MultipartFile[] images,
                                           Error errors,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Pair<Portfolio, Planner> info = portfolioService.updatePortfolio(request, userDetails.getUser().getId());

        imageItemService.updateImage(images, info.getFirst(), info.getSecond());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("/portfolios")
    public ResponseEntity<?> deletePortfolio(@AuthenticationPrincipal CustomUserDetails userDetails) {
        portfolioService.deletePortfolio(userDetails.getInfo());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/myportfolio")
    public ResponseEntity<?> myPortfolio(@AuthenticationPrincipal CustomUserDetails userDetails) {
        PortfolioResponse.MyPortfolioDTO myPortfolio = portfolioService.myPortfolio(userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(myPortfolio));
    }
}
