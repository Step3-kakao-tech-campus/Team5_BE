package com.kakao.sunsuwedding.review;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.user.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewJPARepository reviewJPARepository;
    private final MatchJPARepository matchJPARepository;

    @Transactional
    public void addReview(Pair<String, Long> info, ReviewRequest.AddDTO request) {
        Match match = matchJPARepository.findByChatId(request.chatId()).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND)
        );

        matchConfirmedCheck(match);
        permissionCheck(info, match);

        reviewJPARepository.save(
                Review.builder()
                    .match(match)
                    .content(request.content())
                    .build()
        );
    }

    public ReviewResponse.ReviewDTO findByReviewId(Long reviewId) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        return new ReviewResponse.ReviewDTO(review.id, review.content);
    }

    public ReviewResponse.FindAllByUserDTO findAllByUser(Pair<String, Long> info) {
        String role = info.getFirst();
        Long userId = info.getSecond();

        List<Review> reviews;
        if (role.equals(Role.PLANNER.getRoleName())) {
            reviews = reviewJPARepository.findAllByMatchPlannerId(userId);
        }
        else {
            reviews = reviewJPARepository.findAllByMatchCoupleId(userId);
        }

        List<ReviewResponse.ReviewDTO> reviewDTOS = ReviewDTOConverter.toFindAllByUserDTO(reviews);

        return new ReviewResponse.FindAllByUserDTO(reviewDTOS);
    }

    @Transactional
    public void updateReview(Pair<String, Long> info, Long reviewId, ReviewRequest.UpdateDTO request) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        permissionCheck(info, review.getMatch());

        review.updateContent(request.content());
        reviewJPARepository.save(review);
    }

    @Transactional
    public void deleteReview(Pair<String, Long> info, Long reviewId) {
        Review review = reviewJPARepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException(BaseException.REVIEW_NOT_FOUND)
        );

        permissionCheck(info, review.getMatch());

        reviewJPARepository.delete(review);
    }

    private void permissionCheck(Pair<String, Long> info, Match match) {
        String role = info.getFirst();
        Long userId = info.getSecond();

        if (role.equals(Role.PLANNER) || !match.getCouple().getId().equals(userId)) {
            throw new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS);
        }
    }

    private void matchConfirmedCheck(Match match) {
        if (!match.getStatus().equals(MatchStatus.CONFIRMED)) {
            throw new BadRequestException(BaseException.MATCHING_NOT_CONFIRMED);
        }
    }
}
