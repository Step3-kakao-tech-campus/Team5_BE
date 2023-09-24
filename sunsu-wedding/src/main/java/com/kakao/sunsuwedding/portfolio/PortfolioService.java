package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioInsertRequest;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioListItemDTO;
import com.kakao.sunsuwedding.portfolio.dto.PriceDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioUpdateRequest;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public List<PortfolioListItemDTO> getPortfolios(PageRequest pageRequest) {
        Page<Portfolio> portfolios = portfolioRepository.findAll(pageRequest);

        // TODO: 포트폴리오별 대표 이미지 불러오기
        Resource[] images = null;

        return PortfolioDTOConverter.toListItemDTO(portfolios, images);
    }

    public PortfolioDTO getPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 포트폴리오를 찾을 수 없습니다."));

        // TODO: ImageItemService 에서 포트폴리오 이미지 가져오기
        Resource[] images = null;

        // TODO: PriceItemService 에서 가격 정보 가져오기
        PriceDTO priceDTO = null;

        return PortfolioDTOConverter.toPortfolioDTO(portfolio, images, priceDTO);
    }

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
