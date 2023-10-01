package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Base64;

public class ImageEncoder {
    public static String encode(ImageItem imageItem) {
        Resource resource = new FileSystemResource(imageItem.getFilePath() + imageItem.getOriginFileName());
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
