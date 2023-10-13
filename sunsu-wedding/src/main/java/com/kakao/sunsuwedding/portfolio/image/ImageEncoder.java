package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding._core.errors.exception.Exception500;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class ImageEncoder {
    public static String encode(ImageItem imageItem) {
        Resource resource = new FileSystemResource(imageItem.getFilePath());
        if (!resource.exists()) {
            throw new Exception404(BaseException.PORTFOLIO_IMAGE_NOT_FOUND.getMessage());
        }

        try {
            return Base64.getEncoder().encodeToString(resource.getContentAsByteArray());
        }
        catch (IOException exception) {
            throw new Exception500(BaseException.PORTFOLIO_IMAGE_ENCODING_ERROR.getMessage());
        }
    }

    public static List<String> encode(List<Portfolio> portfolios, List<ImageItem> imageItems) {
        return portfolios
                .stream()
                .map(item -> imageItems
                        .stream()
                        .filter(imageItem -> imageItem.getPortfolio().getId().equals(item.getId()))
                        .findFirst()
                        .map(ImageEncoder::encode)
                        .orElseGet(String::new))
                .toList();
    }
}
