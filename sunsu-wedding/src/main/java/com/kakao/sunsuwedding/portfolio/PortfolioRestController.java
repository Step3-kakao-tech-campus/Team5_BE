package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.portfolio.cursor.PageCursor;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioRestController {
    private final PortfolioServiceImpl portfolioServiceImpl;

    private static final int PAGE_SIZE = 10;

    @PostMapping(value = "")
    public ResponseEntity<?> addPortfolio(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody @Valid PortfolioRequest.AddDTO request) {
        portfolioServiceImpl.addPortfolio(request, userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping(value = "")
    public ResponseEntity<?> findPortfolios(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestParam(defaultValue = "-1") @Min(-2) Long cursor,
                                            @RequestParam(defaultValue = "null") String name,
                                            @RequestParam(defaultValue = "null") String location,
                                            @RequestParam(defaultValue = "null") String minPrice,
                                            @RequestParam(defaultValue = "null") String maxPrice) {

        Long userId = (userDetails == null) ? -1 : userDetails.getUser().getId();
        Long min = minPrice.equals("null") ? -1L : Long.valueOf(minPrice);
        Long max = maxPrice.equals("null") ? -1L : Long.valueOf(maxPrice);
        CursorRequest cursorRequest = new CursorRequest(cursor, PAGE_SIZE, name, location, min, max);
        PageCursor<List<PortfolioResponse.FindAllDTO>> response = portfolioServiceImpl.findPortfolios(cursorRequest, userId);

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findPortfolioById(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable @Min(1) Long id) {

        Long userId = (userDetails == null) ? -1 : userDetails.getUser().getId();
        PortfolioResponse.FindByIdDTO portfolio = portfolioServiceImpl.findPortfolioById(id, userId);
        return ResponseEntity.ok().body(ApiUtils.success(portfolio));
    }

    @PutMapping(value = "")
    public ResponseEntity<?> updatePortfolio(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Valid @RequestBody PortfolioRequest.UpdateDTO request) {
        portfolioServiceImpl.updatePortfolio(request, userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deletePortfolio(@AuthenticationPrincipal CustomUserDetails userDetails) {
        portfolioServiceImpl.deletePortfolio(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping("/self")
    public ResponseEntity<?> myPortfolio(@AuthenticationPrincipal CustomUserDetails userDetails) {
        PortfolioResponse.MyPortfolioDTO myPortfolio = portfolioServiceImpl.myPortfolio(userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(myPortfolio));
    }
}
