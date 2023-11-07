package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.portfolio.cursor.PageCursor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolios")
public class PortfolioRestController {
    private final PortfolioService portfolioService;

    private static final int PAGE_SIZE = 10;

    @PostMapping(value = "")
    public ResponseEntity<?> addPortfolio(@RequestBody PortfolioRequest.AddDTO request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        portfolioService.addPortfolio(request, userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping(value = "")
    public ResponseEntity<?> findPortfolios(@RequestParam(defaultValue = "-1") @Min(-2) Long cursor,
                                           @RequestParam @Nullable String name,
                                           @RequestParam @Nullable String location,
                                           @RequestParam @Nullable Long minPrice,
                                           @RequestParam @Nullable Long maxPrice,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = (userDetails == null) ? -1 : userDetails.getUser().getId();
        CursorRequest cursorRequest = new CursorRequest(cursor, PAGE_SIZE, name, location, minPrice, maxPrice);
        PageCursor<List<PortfolioResponse.FindAllDTO>> response = portfolioService.findPortfolios(cursorRequest, userId);

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findPortfolioById(@PathVariable @Min(1) Long id,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = (userDetails == null) ? -1 : userDetails.getUser().getId();
        PortfolioResponse.FindByIdDTO portfolio = portfolioService.findPortfolioById(id, userId);
        return ResponseEntity.ok().body(ApiUtils.success(portfolio));
    }

    @PutMapping(value = "")
    public ResponseEntity<?> updatePortfolio(@RequestBody PortfolioRequest.UpdateDTO request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        portfolioService.updatePortfolio(request, userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deletePortfolio(@AuthenticationPrincipal CustomUserDetails userDetails) {
        portfolioService.deletePortfolio(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/self")
    public ResponseEntity<?> myPortfolio(@AuthenticationPrincipal CustomUserDetails userDetails) {
        PortfolioResponse.MyPortfolioDTO myPortfolio = portfolioService.myPortfolio(userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(myPortfolio));
    }
}
