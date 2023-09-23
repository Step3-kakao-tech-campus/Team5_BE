package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioUpdateRequest;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    public void updatePortfolio(Planner planner, PortfolioUpdateRequest request) {
        Portfolio portfolio = portfolioRepository.findByPlanner(planner)
                .orElseThrow(() -> new Exception404("포트폴리오가 존재하지 않기 때문에 업데이트를 할 수 없습니다."));

        portfolio.updateTitle(request.title());
        portfolio.updateDescription(request.description());
        portfolio.updateLocation(request.location());
        portfolio.updateCareer(request.career());
        portfolio.updatePartnerCompany(request.partnerCompany());

        // TODO: PriceItemService를 통해 가격 업데이트
    }
}
