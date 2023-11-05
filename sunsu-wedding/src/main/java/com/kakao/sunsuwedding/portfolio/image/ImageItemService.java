package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ServerException;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.kakao.sunsuwedding._core.constants.Constants.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageItemService {
    private static final Logger logger = LoggerFactory.getLogger(ImageItemService.class);

    private final ImageItemJPARepository imageItemJPARepository;
    private final ImageItemJDBCRepository imageItemJDBCRepository;

    public void uploadImage(MultipartFile[] images, Portfolio portfolio, Planner planner) {
        validateImages(images);
        String path = generateDirectoryPath(planner.getId(), planner.getUsername());
        makeDirectory(path);
        storeImagesInServerAndDatabase(images, portfolio, path);
    }

    public void updateImage(MultipartFile[] images, Portfolio portfolio, Planner planner) {
        validateImages(images);
        String path = generateDirectoryPath(planner.getId(), planner.getUsername());
        File directory = makeDirectory(path);
        cleanExistedImage(directory, portfolio.getId());
        storeImagesInServerAndDatabase(images, portfolio, path);
    }

    private void validateImages(MultipartFile[] images) {
        if (images.length > 5) throw new BadRequestException(BaseException.PORTFOLIO_IMAGE_COUNT_EXCEED);
    }

    private String generateDirectoryPath(Long plannerId, String username) {
        // ex) ../kakao/sunsuwedding/gallery/1_meta/
        return SYSTEM_DIRECTORY + SYSTEM_SEPARATOR + "gallery" + SYSTEM_SEPARATOR + plannerId + "_" + username + SYSTEM_SEPARATOR;
    }

    private File makeDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            // 디렉토리가 존재하지 않으면 생성
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                // 디렉토리 생성에 실패한 경우 예외 처리
                throw new ServerException(BaseException.PORTFOLIO_CREATE_DIRECTORY_ERROR);
            }
        }
        return directory;
    }

    private void storeImagesInServerAndDatabase(MultipartFile[] images, Portfolio portfolio, String directoryPath) {
        List<ImageItem> imageItems = new ArrayList<>();
        for (MultipartFile image : images) {
            String uploadImagePath = makeImageFile(directoryPath, image);
            ImageItem imageItem = ImageItem.builder()
                    .portfolio(portfolio)
                    .originFileName(image.getOriginalFilename())
                    .filePath(uploadImagePath)
                    .fileSize(image.getSize())
                    .thumbnail(image == images[0])
                    .build();
            imageItems.add(imageItem);
        }
        imageItemJDBCRepository.batchInsertImageItems(imageItems);
    }

    private String makeImageFile(String directoryPath, MultipartFile image) {
        try {
            // 이미지 파일 생성
            String originalImageName = image.getOriginalFilename();
            String NameWithoutExtension = originalImageName.split("\\.")[0];
            String uploadImageName = UUID.randomUUID() + "(" + NameWithoutExtension + ")";
            String uploadImagePath = directoryPath + uploadImageName;
            image.transferTo(new File(uploadImagePath));
            logger.debug("Trying to process image: {}", image.getOriginalFilename());

            return uploadImagePath;
        }
        catch (IOException e) {
            logger.error("Failed to process image", e);
            throw new ServerException(BaseException.PORTFOLIO_IMAGE_CREATE_ERROR);
        }
    }

    private void cleanExistedImage(File directory, Long portfolioId) {
        // 이미지 파일을 Base64로 인코딩하고 나니까 확장자가 사라져서
        // 일단 그대로 디렉토리 내 파일 일괄 삭제하는 로직
        try {
            FileUtils.cleanDirectory(directory);
        }
        catch (Exception e) {throw new ServerException(BaseException.PORTFOLIO_CLEAN_DIRECTORY_ERROR);}

        // TODO: 삭제할 이미지 데이터가 존재하지 않는 경우 예외처리
        imageItemJPARepository.deleteAllByPortfolioId(portfolioId);
    }
}