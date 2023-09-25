package com.kakao.sunsuwedding.portfolio;

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
import org.springframework.data.domain.Page;
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
        Page<Portfolio> portfolios = portfolioRepository.findAll(pageRequest);

        List<Resource> images = imageItemRepository.findAllThumbnailsByPortfolio(portfolios.getContent())
                .stream()
                .map(PortfolioService::getImageResource)
                .toList();

        return PortfolioDTOConverter.toListItemDTO(portfolios, images);
    }

    public PortfolioDTO getPortfolioById(Long id) {
        /*
        여기서 고민해야할 부분

        포트폴리오는 아래 ImageItem 과 PriceItem 을 조회할 때 join 하면 불러올 수 있는 정보
        따라서 따로 조회하지 않아도 불러올 수 있지만,

        fetch join 으로 포트폴리오를 불러오는 시간과 select 쿼리로 포트폴리오를 불러오는 시간
        둘 중에 어떤 것이 더 좋은지 결정해야 한다.

        우선 요청으로 포트폴리오 id만 넘겨받기 때문에
        해당 포트폴리오가 존재하는지 검사하는 부분도 필요할 것 같기도 하지만

        이미지는 1장 이상을 필수로 등록하는 것을 원칙으로 한다면
        ImageItem 을 검색해서 empty 여부를 검사하여 이를 알아낼 수 있긴 하다
         */
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 포트폴리오를 찾을 수 없습니다."));

        List<Resource> images = imageItemRepository.findByPortfolioId(id)
                .stream()
                .map(PortfolioService::getImageResource)
                .toList();

        List<PriceItem> priceItems = priceItemRepository.findAllByPortfolioId(id);
        List<PriceItemDTO> priceItemDTOS = toPriceItemDTOS(priceItems);

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
        portfolioRepository.deleteByPlanner(planner);
        imageItemRepository.deleteAllByPortfolioPlannerId(planner.getId());
        priceItemRepository.deleteAllByPortfolioPlannerId(planner.getId());
    }

    private static List<PriceItemDTO> toPriceItemDTOS(List<PriceItem> priceItems) {
        return priceItems
                .stream()
                .map(priceItem -> new PriceItemDTO(priceItem.getItemTitle(), priceItem.getItemPrice()))
                .toList();
    }

    private static Resource getImageResource(ImageItem imageItem) {
        return new FileSystemResource(imageItem.getFilePath() + imageItem.getOriginalFileName());
    }
}
