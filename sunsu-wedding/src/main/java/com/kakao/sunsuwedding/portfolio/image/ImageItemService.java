package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception500;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageItemService {
    private static final Logger logger = LoggerFactory.getLogger(ImageItemService.class);

    private final ImageItemJPARepository imageItemJPARepository;

    public void uploadImage(MultipartFile[] images, Portfolio portfolio, Planner planner) {

        // 저장 경로 설정
        String separator = System.getProperty("file.separator");
        String baseDirectory = System.getProperty("user.dir") + separator + "gallery" + separator;
        String uploadDirectory = baseDirectory + planner.getUsername() + separator;

        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // 디렉토리가 존재하지 않으면 생성
            if (!created) {
                // 디렉토리 생성에 실패한 경우 예외 처리
                throw new Exception500("디렉토리 생성에 실패했습니다.");
            }
        }

        // 이미지 생성 및 DB 저장
        for (MultipartFile image : images) {
            try {
                String originalImageName = image.getOriginalFilename();
                String uploadImageName = UUID.randomUUID() + "_" + originalImageName;
                String uploadImagePath = uploadDirectory + uploadImageName;
                image.transferTo(new File(uploadImagePath));
                logger.debug("Trying to process image: {}", image.getOriginalFilename());

                // TODO: Thumbnail인지 아닌지 확인하고 저장하는 로직

                boolean thumbnail = false;
                if (image == images[0]) {
                    thumbnail = true;
                }

                ImageItem imageItem = ImageItem.builder()
                        .portfolio(portfolio)
                        .originFileName(originalImageName)
                        .filePath(uploadImagePath)
                        .fileSize(image.getSize())
                        .thumbnail(thumbnail)
                        .build();
                imageItemJPARepository.save(imageItem);
            }
            catch (Exception e) {
                logger.error("Failed to process image", e);
                throw new Exception500("이미지 처리에 실패했습니다.");
            }
        }
    }

    @Transactional
    public void updateImage(MultipartFile[] images, Portfolio portfolio, Planner planner) {
        // 저장 경로 설정
        String separator = System.getProperty("file.separator");
        String baseDirectory = System.getProperty("user.dir") +separator + "gallery" + separator;
        String uploadDirectory = baseDirectory + planner.getUsername() + separator;

        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // 디렉토리가 존재하지 않으면 생성
            if (!created) {
                // 디렉토리 생성에 실패한 경우 예외 처리
                throw new Exception500("디렉토리 생성에 실패했습니다.");
            }
        }

        // 기존의 서버 이미지 파일 및 DB 메타데이터 삭제
        try {
            FileUtils.cleanDirectory(directory);
        }
        catch (Exception e) {throw new Exception500("디렉토리 비우기에 실패하였습니다.");}
        // TODO: 삭제할 이미지 데이터가 존재하지 않는 경우 예외처리
        imageItemJPARepository.deleteAllByPortfolioId(portfolio.getId());

        // 이미지 생성 및 DB 저장
        for (MultipartFile image : images) {
            try {
                String originalImageName = image.getOriginalFilename();
                String uploadImageName = UUID.randomUUID() + "_" + originalImageName;
                String uploadImagePath = uploadDirectory + uploadImageName;
                image.transferTo(new File(uploadImagePath));
                logger.debug("Trying to process image: {}", image.getOriginalFilename());

                // TODO: Thumbnail인지 아닌지 확인하고 저장하는 로직

                boolean thumbnail = false;
                if (image == images[0]) {
                    thumbnail = true;
                }

                ImageItem imageItem = ImageItem.builder()
                        .portfolio(portfolio)
                        .originFileName(originalImageName)
                        .filePath(uploadImagePath)
                        .fileSize(image.getSize())
                        .thumbnail(thumbnail)
                        .build();
                imageItemJPARepository.save(imageItem);
            }
            catch (Exception e) {
                logger.error("Failed to process image", e);
                throw new Exception500("이미지 처리에 실패했습니다.");
            }
        }
    }
}
