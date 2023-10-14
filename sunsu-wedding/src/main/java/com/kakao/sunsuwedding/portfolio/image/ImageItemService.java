package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding._core.errors.BaseException;
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

@Service
@Transactional
@RequiredArgsConstructor
public class ImageItemService {
    private static final Logger logger = LoggerFactory.getLogger(ImageItemService.class);

    private final ImageItemJPARepository imageItemJPARepository;

    private String setDirectoryPath(Long id, String username) {
        String separator = System.getProperty("file.separator");
        String baseDirectory = System.getProperty("user.dir") + separator + "gallery" + separator;
        String uploadDirectory = baseDirectory + id + "_" + username + separator;

        return uploadDirectory;
    }

    private File makeDirectory(String uploadDirectory) {
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            // 디렉토리가 존재하지 않으면 생성
            boolean created = directory.mkdirs();
            if (!created) {
                // 디렉토리 생성에 실패한 경우 예외 처리
                throw new ServerException(BaseException.PORTFOLIO_CREATE_DIRECTORY_ERROR);
            }
        }
        return directory;
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

    private String makeImageFile(String uploadDirectory, MultipartFile image) {
        try {
            // 이미지 파일 생성
            String originalImageName = image.getOriginalFilename();
            String NameWithoutExtension = originalImageName.split("\\.")[0];
            String uploadImageName = UUID.randomUUID() + "(" + NameWithoutExtension + ")";
            String uploadImagePath = uploadDirectory + uploadImageName;
            image.transferTo(new File(uploadImagePath));
            logger.debug("Trying to process image: {}", image.getOriginalFilename());

            return uploadImagePath;
        }
        catch (IOException e) {
            logger.error("Failed to process image", e);
            throw new ServerException(BaseException.PORTFOLIO_IMAGE_CREATE_ERROR);
        }
    }

    private void saveImage(MultipartFile[] images, Portfolio portfolio, String uploadDirectory) {
        List<ImageItem> imageItems = new ArrayList<>();
        for (MultipartFile image : images) {
            String uploadImagePath = makeImageFile(uploadDirectory, image);
            ImageItem imageItem = ImageItem.builder()
                    .portfolio(portfolio)
                    .originFileName(image.getOriginalFilename())
                    .filePath(uploadImagePath)
                    .fileSize(image.getSize())
                    .thumbnail(image == images[0])
                    .build();
            imageItems.add(imageItem);
        }
        imageItemJPARepository.saveAll(imageItems);
    }


    public void uploadImage(MultipartFile[] images, Portfolio portfolio, Planner planner) {
        // 저장 경로 설정 (root -> gallery -> {userId}_{username} 폴더)
        String uploadDirectory = setDirectoryPath(planner.getId(), planner.getUsername());
        makeDirectory(uploadDirectory);

        // 이미지 생성 및 DB 저장
        saveImage(images, portfolio, uploadDirectory);
    }

    @Transactional
    public void updateImage(MultipartFile[] images, Portfolio portfolio, Planner planner) {
        // 저장 경로 설정 (root -> gallery -> {userId}_{username} 폴더)
        String uploadDirectory = setDirectoryPath(planner.getId(), planner.getUsername());
        File directory = makeDirectory(uploadDirectory);

        // 기존의 서버 이미지 파일 및 DB 메타데이터 삭제
        cleanExistedImage(directory, portfolio.getId());

        // 이미지 생성 및 DB 저장
        saveImage(images, portfolio, uploadDirectory);
    }


}
