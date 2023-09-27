package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.portfolio.dto.*;
import com.kakao.sunsuwedding.portfolio.image.ImageItem;
import com.kakao.sunsuwedding.portfolio.image.ImageItemRepository;
import com.kakao.sunsuwedding.portfolio.price.PriceCalculator;
import com.kakao.sunsuwedding.portfolio.price.PriceItem;
import com.kakao.sunsuwedding.portfolio.price.PriceItemRepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
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
                .map(PortfolioService::getBase64EncodedImage)
                .toList();

        return PortfolioDTOConverter.toListItemDTO(portfolios, images);
    }

    public PortfolioDTO getPortfolioById(Long id) {
        List<ImageItem> imageItems = imageItemRepository.findByPortfolioId(id);
        if (imageItems.isEmpty()) {
            throw new Exception404(BaseException.PORTFOLIO_NOT_FOUND.getMessage());
        }

        Portfolio portfolio = imageItems.get(0).getPortfolio();
        List<String> images = imageItems
                .stream()
                .map(PortfolioService::getBase64EncodedImage)
                .toList();

        if (images.isEmpty()) {
            throw new Exception404(BaseException.PORTFOLIO_IMAGE_NOT_FOUND.getMessage());
        }

        List<PriceItem> priceItems = priceItemRepository.findAllByPortfolioId(id);
        List<PriceItemDTO> priceItemDTOS = PortfolioDTOConverter.toPriceItemDTOS(priceItems);

        Long totalPrice = PriceCalculator.execute(priceItemDTOS);
        PriceDTO priceDTO = new PriceDTO(totalPrice, priceItemDTOS);

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

    public void deletePortfolio(Planner planner) {
        priceItemRepository.deleteAllByPortfolioPlannerId(planner.getId());
        imageItemRepository.deleteAllByPortfolioPlannerId(planner.getId());
        portfolioRepository.deleteByPlanner(planner);
    }

    private static String getBase64EncodedImage(ImageItem imageItem) {
        Resource resource = new FileSystemResource(imageItem.getFilePath() + imageItem.getOriginalFileName());
        if (!resource.exists()) {
            throw new Exception404(BaseException.PORTFOLIO_IMAGE_NOT_FOUND.getMessage());
        }

        try {
            return Base64.getEncoder().encodeToString(resource.getContentAsByteArray());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
