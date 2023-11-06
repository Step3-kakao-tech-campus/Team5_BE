package com.kakao.sunsuwedding.review.image;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding.portfolio.image.PortfolioImageItem;
import com.kakao.sunsuwedding.review.Review;
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
public class ReviewImageItemService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewImageItemService.class);

    private final ReviewImageItemJPARepository reviewImageItemJPARepository;
    private final ReviewImageItemJDBCRepository reviewImageItemJDBCRepository;

    @Transactional
    public void uploadImage(List<String> imageItems, Review review) {
        //if (imageItems.size() > 5) throw new BadRequestException(BaseException.REVIEW_IMAGE_COUNT_EXCEED);
        storeImagesInDatabase(imageItems, review);
    }

    @Transactional
    public void updateImage(List<String> imageItems, Review review) {
        //if (imageItems.size() > 5) throw new BadRequestException(BaseException.REVIEW_IMAGE_COUNT_EXCEED);
        clearImagesInDatabase(review.getId());
        storeImagesInDatabase(imageItems, review);
    }

    private void clearImagesInDatabase(Long reviewId) {
        reviewImageItemJPARepository.deleteAllByReviewId(reviewId);
    }

    private void storeImagesInDatabase(List<String> imageItems, Review review) {
        List<ReviewImageItem> reviewImageItems = new ArrayList<>();
        for (String imageItem : imageItems) {
            ReviewImageItem reviewImageItem = ReviewImageItem.builder()
                    .review(review)
                    .image(imageItem)
                    .thumbnail(imageItem.equals(imageItems.get(0)))
                    .build();
            reviewImageItems.add(reviewImageItem);
        }
        reviewImageItemJDBCRepository.batchInsertImageItems(reviewImageItems);
    }
}
