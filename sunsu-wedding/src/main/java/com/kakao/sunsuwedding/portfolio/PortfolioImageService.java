package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding._core.errors.exception.Exception500;
import com.kakao.sunsuwedding.portfolio.image.ImageItem;
import com.kakao.sunsuwedding.portfolio.image.ImageItemJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PortfolioImageService {
    private final ImageItemJPARepository itemJPARepository;
    private static final String UPLOAD_DIR = File.separator + "image" + File.separator + "uploads" + File.separator;
    public void uploadImage(MultipartFile[] images, Portfolio portfolio, Planner planner) {
        for (MultipartFile image : images) {
            try {
                String originalImageName = image.getOriginalFilename();
                String uploadImageName = UUID.randomUUID() + "_" + originalImageName;
                String uploadImagePath = UPLOAD_DIR + uploadImageName;;
                image.transferTo(new File(uploadImagePath));

                ImageItem imageItem = ImageItem.builder()
                        .portfolio(portfolio)
                        .originFileName(originalImageName)
                        .filePath(uploadImagePath)
                        .fileSize(image.getSize())
                        .build();
                itemJPARepository.save(imageItem);
            }
            catch (Exception e) {
                throw new Exception500("이미지 처리에 실패했습니다.");
            }
        }
    }
}
