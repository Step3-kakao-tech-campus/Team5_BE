package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioImageItemService {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioImageItemService.class);

    private final PortfolioImageItemJPARepository portfolioImageItemJPARepository;
    private final PortfolioImageItemJDBCRepository portfolioImageItemJDBCRepository;

    @Transactional
    public void uploadImage(List<String> imageItems, Portfolio portfolio) {
        if (imageItems.size() > 5) throw new BadRequestException(BaseException.PORTFOLIO_IMAGE_COUNT_EXCEED);
        storeImagesInDatabase(imageItems, portfolio);
    }

    @Transactional
    public void updateImage(List<String> imageItems, Portfolio portfolio) {
        if (imageItems.size() > 5) throw new BadRequestException(BaseException.PORTFOLIO_IMAGE_COUNT_EXCEED);
        clearImagesInDatabase(portfolio.getId());
        storeImagesInDatabase(imageItems, portfolio);
    }

    private void clearImagesInDatabase(Long portfolioId) {
        portfolioImageItemJPARepository.deleteAllByPortfolioId(portfolioId);
    }

    private void storeImagesInDatabase(List<String> imageItems, Portfolio portfolio) {
        List<PortfolioImageItem> portfolioImageItems = new ArrayList<>();
        for (String imageItem : imageItems) {
            PortfolioImageItem portfolioImageItem = PortfolioImageItem.builder()
                    .portfolio(portfolio)
                    .image(imageItem)
                    .thumbnail(imageItem.equals(imageItems.get(0)))
                    .build();
            portfolioImageItems.add(portfolioImageItem);
        }
        portfolioImageItemJDBCRepository.batchInsertImageItems(portfolioImageItems);
    }
}