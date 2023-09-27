package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioInsertRequest;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioListItemDTO;
import com.kakao.sunsuwedding.portfolio.dto.PortfolioUpdateRequest;
import com.kakao.sunsuwedding.portfolio.image.ImageEncoder;
import com.kakao.sunsuwedding.portfolio.image.ImageItem;
import com.kakao.sunsuwedding.portfolio.image.ImageItemRepository;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.portfolio.price.PriceItemRepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final ImageItemRepository imageItemRepository;
    private final PriceItemRepository priceItemRepository;

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
        List<Portfolio> portfolios = portfolioRepository.findAll(pageRequest).getContent();

        List<String> images = imageItemRepository.findAllByThumbnailAndPortfolioIn(true, portfolios)
                .stream()
                .map(ImageEncoder::encode)
                .toList();

        return PortfolioDTOConverter.toListItemDTO(portfolios, images);
    }

    public PortfolioDTO getPortfolioById(Long id) {
        List<ImageItem> imageItems = imageItemRepository.findByPortfolioId(id);
        if (imageItems.isEmpty()) {
            throw new Exception404(BaseException.PORTFOLIO_NOT_FOUND.getMessage());
        }

        List<String> images = imageItems
                .stream()
                .map(ImageEncoder::encode)
                .toList();

        List<PriceItem> priceItems = priceItemRepository.findAllByPortfolioId(id);
        Portfolio portfolio = imageItems.get(0).getPortfolio();
        return PortfolioDTOConverter.toPortfolioDTO(portfolio, images, priceItems);
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

    public void deletePortfolio(Planner planner) {
        priceItemRepository.deleteAllByPortfolioPlannerId(planner.getId());
        imageItemRepository.deleteAllByPortfolioPlannerId(planner.getId());
        portfolioRepository.deleteByPlanner(planner);
    }
}
