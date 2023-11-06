package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ServerException;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.portfolio.PortfolioRequest;
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
public class PortfolioImageItemService {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioImageItemService.class);

    private final PortfolioImageItemJPARepository portfolioImageItemJPARepository;
    private final PortfolioImageItemJDBCRepository portfolioImageItemJDBCRepository;

    @Transactional
    public void uploadImage(MultipartFile[] images, Portfolio portfolio, Planner planner) {
        if (images.length > 5) throw new BadRequestException(BaseException.PORTFOLIO_IMAGE_COUNT_EXCEED);

        String path = generateDirectoryPath(planner.getId(), planner.getUsername());
        makeDirectory(path);
        storeImagesInServerAndDatabase(images, portfolio, path);
    }

    @Transactional
    public void updateImage(List<PortfolioRequest.ImagePathDTO> imagePathDTOS, MultipartFile[] images, Portfolio portfolio, Planner planner) {
        String path = generateDirectoryPath(planner.getId(), planner.getUsername());
        File directory = makeDirectory(path);

        for (PortfolioRequest.ImagePathDTO dto : imagePathDTOS) {
            if (!dto.isValid()) {
                portfolioImageItemJPARepository.deleteByPath(dto.path());
                File existedImage = new File(dto.path());
                if (existedImage.exists()) existedImage.delete();
                else throw new ServerException(BaseException.PORTFOLIO_IMAGE_DELETE_ERROR);
            }
        }
        storeImagesInServerAndDatabase(images, portfolio, path);
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
        List<PortfolioImageItem> portfolioImageItems = new ArrayList<>();
        for (MultipartFile image : images) {
            String uploadImagePath = makeImageFile(directoryPath, image);
            PortfolioImageItem portfolioImageItem = PortfolioImageItem.builder()
                    .portfolio(portfolio)
                    .originFileName(image.getOriginalFilename())
                    .filePath(uploadImagePath)
                    .fileSize(image.getSize())
                    .thumbnail(image == images[0])
                    .build();
            portfolioImageItems.add(portfolioImageItem);
        }
        portfolioImageItemJDBCRepository.batchInsertImageItems(portfolioImageItems);
    }

    private String makeImageFile(String directoryPath, MultipartFile image) {
        try {
            // 이미지 파일 생성
            String uploadImagePath = directoryPath + generateImageFilePath(image.getOriginalFilename());
            image.transferTo(new File(uploadImagePath));
            logger.debug("Trying to process image: {}", image.getOriginalFilename());

            return uploadImagePath;
        }
        catch (IOException e) {
            logger.error("Failed to process image", e);
            throw new ServerException(BaseException.PORTFOLIO_IMAGE_CREATE_ERROR);
        }
    }

    private String generateImageFilePath(String originalFilename) {
        if (originalFilename == null)
            throw new ServerException(BaseException.PORTFOLIO_IMAGE_CREATE_ERROR);

        // ex) 7423db03-2deb-411d-a26e-8caad9793077(original_image_1).jpg
        return UUID.randomUUID() + "(" + originalFilename.split("\\.")[0] + ")." + originalFilename.split("\\.")[1];
    }

    private void cleanExistedImage(File directory, Long portfolioId) {
        // 이미지 파일을 Base64로 인코딩하고 나니까 확장자가 사라져서
        // 일단 그대로 디렉토리 내 파일 일괄 삭제하는 로직
        try {
            FileUtils.cleanDirectory(directory);
        }
        catch (Exception e) {throw new ServerException(BaseException.PORTFOLIO_CLEAN_DIRECTORY_ERROR);}

        // TODO: 삭제할 이미지 데이터가 존재하지 않는 경우 예외처리
        portfolioImageItemJPARepository.deleteAllByPortfolioId(portfolioId);
    }
}