package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.portfolio.cursor.CursorRequest;
import com.kakao.sunsuwedding.portfolio.cursor.PageCursor;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.planner.Planner;

import java.util.List;

public interface PortfolioService {

    void addPortfolio(PortfolioRequest.AddDTO request, Long plannerId);

    PageCursor<List<PortfolioResponse.FindAllDTO>> findPortfolios(CursorRequest request, Long userId);

    PortfolioResponse.FindByIdDTO findPortfolioById(Long portfolioId, Long userId);

    void updatePortfolio(PortfolioRequest.UpdateDTO request, Long plannerId);

    void deletePortfolio(User user);

    PortfolioResponse.MyPortfolioDTO myPortfolio(Long plannerId);

    void updateConfirmedPrices(Planner planner);

    void updateAvgStars(Planner planner);
}
