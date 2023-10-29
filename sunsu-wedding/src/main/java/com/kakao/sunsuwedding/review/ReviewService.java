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
    public void addReview(Pair<String, Long> info, Long chatId, ReviewRequest.AddDTO request) {
        Match match = matchJPARepository.findByChatId(chatId).orElseThrow(
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

    public ReviewResponse.FindAllByChatIdDTO findAllByChatId(Long chatId) {
        List<Review> reviews = reviewJPARepository.findAllByMatchChatId(chatId);

        List<ReviewResponse.ReviewDTO> reviewDTOS = ReviewDTOConverter.toFindAllByChatIdDTO(reviews);

        return new ReviewResponse.FindAllByChatIdDTO(reviewDTOS);
    }

    public ReviewResponse.FindAllByUserDTO findAllByUser(Pair<String, Long> info) {
        String role = info.getFirst();
        Long userId = info.getSecond();

        List<Review> reviews = getReviewsByUser(role, userId);
        List<ReviewResponse.ReviewWithNameDTO> reviewDTOS = getReviewDTOS(role, reviews);

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

    private List<Review> getReviewsByUser(String role, Long userId) {
        return (role.equals(Role.PLANNER.getRoleName())) ?
                reviewJPARepository.findAllByMatchPlannerId(userId) :
                reviewJPARepository.findAllByMatchCoupleId(userId);
    }

    private List<ReviewResponse.ReviewWithNameDTO> getReviewDTOS (String role, List<Review> reviews) {
        return (role.equals(Role.PLANNER.getRoleName())) ?
                ReviewDTOConverter.toFindAllByPlannerDTO(reviews) :
                ReviewDTOConverter.toFindAllByCoupleDTO(reviews);
    }
}
