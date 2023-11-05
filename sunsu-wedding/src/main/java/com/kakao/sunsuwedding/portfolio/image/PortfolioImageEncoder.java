package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.errors.exception.ServerException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class PortfolioImageEncoder {
    public static String encode(PortfolioImageItem portfolioImageItem) {
        Resource resource = new FileSystemResource(portfolioImageItem.getFilePath());
        if (!resource.exists()) {
            throw new NotFoundException(BaseException.PORTFOLIO_IMAGE_NOT_FOUND);
        }

        try {
            return Base64.getEncoder().encodeToString(resource.getContentAsByteArray());
        }
        catch (IOException exception) {
            throw new ServerException(BaseException.PORTFOLIO_IMAGE_ENCODING_ERROR);
        }
    }

    public static List<String> encode(List<Portfolio> portfolios, List<PortfolioImageItem> portfolioImageItems) {
        return portfolios
                .stream()
                .map(item -> portfolioImageItems
                        .stream()
                        .filter(imageItem -> imageItem.getPortfolio().getId().equals(item.getId()))
                        .findFirst()
                        .map(PortfolioImageEncoder::encode)
                        .orElseGet(String::new))
                .toList();
    }
}
