package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioInsertRequest;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    public void insertPortfolio(Planner planner, PortfolioInsertRequest request, MultipartFile[] images) {
        Portfolio portfolio = Portfolio.builder()
                .planner(planner)
                .title(request.title())
                .description(request.description())
                .location(request.location())
                .career(request.career())
                .partnerCompany(request.partnerCompany())
                .build();

        // TODO: image 저장

        try {
            portfolioRepository.save(portfolio);
        }
        catch (DataIntegrityViolationException exception) {
            throw new Exception400("");
        }
    }

}
