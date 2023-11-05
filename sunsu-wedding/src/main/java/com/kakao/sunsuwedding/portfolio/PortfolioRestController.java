package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.portfolio.cursor.PageCursor;
import com.kakao.sunsuwedding.portfolio.image.ImageItemService;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portfolios")
public class PortfolioRestController {
    private final PortfolioService portfolioService;
    private final ImageItemService imageItemService;
    private static final int PAGE_SIZE = 10;

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> addPortfolios(@RequestPart PortfolioRequest.AddDTO request,
                                           @RequestPart MultipartFile[] images,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        portfolioService.addPortfolio(request, images, userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @GetMapping(value = "")
    public ResponseEntity<?> getPortfolios(@RequestParam(defaultValue = "-1") @Min(-2) Long cursor,
                                           @RequestParam @Nullable String name,
                                           @RequestParam @Nullable String location,
                                           @RequestParam @Nullable Long minPrice,
                                           @RequestParam @Nullable Long maxPrice,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = (userDetails == null) ? -1 : userDetails.getUser().getId();
        CursorRequest cursorRequest = new CursorRequest(cursor, PAGE_SIZE, name, location, minPrice, maxPrice);
        PageCursor<List<PortfolioResponse.FindAllDTO>> response = portfolioService.getPortfolios(cursorRequest, userId);

        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPortfolioInDetail(@PathVariable @Min(1) Long id,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long userId = (userDetails == null) ? -1 : userDetails.getUser().getId();
        PortfolioResponse.FindByIdDTO portfolio = portfolioService.getPortfolioById(id, userId);
        return ResponseEntity.ok().body(ApiUtils.success(portfolio));
    }

    @PostMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<?> updatePortfolios(@RequestPart PortfolioRequest.UpdateDTO request,
                                           @RequestPart MultipartFile[] images,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        portfolioService.updatePortfolio(request, images, userDetails.getUser().getId());
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
