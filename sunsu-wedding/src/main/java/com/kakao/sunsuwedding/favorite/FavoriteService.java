package com.kakao.sunsuwedding.favorite;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.portfolio.PortfolioJPARepository;
import com.kakao.sunsuwedding.portfolio.image.ImageEncoder;
import com.kakao.sunsuwedding.portfolio.image.ImageItem;
import com.kakao.sunsuwedding.portfolio.image.ImageItemJPARepository;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final UserJPARepository userJPARepository;
    private final PortfolioJPARepository portfolioJPARepository;
    private final ImageItemJPARepository imageItemJPARepository;
    private final FavoriteJPARepository favoriteJPARepository;

    @Transactional
    public void likePortfolio(User user, Long portfolioId){
        User user1 = findByUserId(user.getId());
        Portfolio portfolio = findByPortfolioId(portfolioId);
        Optional<Favorite> favoriteOptional = favoriteJPARepository.findByUserAndPortfolio(user1.getId(), portfolio.getId());
        // 이전에 좋아요 누른 적 있으면 에러 반환
        if (favoriteOptional.isPresent()){
            throw new BadRequestException(BaseException.FAVORITE_ALREADY_EXISTS);
        }
        else {
            // 없을 경우 새로운 좋아요 저장
            favoriteJPARepository.save(
                    Favorite.builder()
                            .user(user1)
                            .portfolio(portfolio)
                            .build()
            );
        }
    }

    @Transactional
    public void unlikePortfolio(User user, Long portfolioId){
        // 이전에 좋아요 누른 적 없으면 에러 반환
        Favorite favorite = favoriteJPARepository.findByUserAndPortfolio(user.getId(), portfolioId).orElseThrow(
                () -> new NotFoundException(BaseException.FAVORITE_NOT_FOUND)
        );
        favoriteJPARepository.delete(favorite);
    }

    public List<FavoriteResponse.FindPortfolioDTO> getFavoritePortfolios(User user, Pageable pageable){
        // userId와 일치하는 favorite의 포트폴리오 내용들 가져옴
        List<Favorite> favoriteList = favoriteJPARepository.findByUserIdFetchJoinPortfolio(user.getId(), pageable);
        List<Portfolio> portfolioList = favoriteList.stream().map(Favorite::getPortfolio).toList();
        List<ImageItem> imageItems = imageItemJPARepository.findAllByThumbnailAndPortfolioInOrderByPortfolioCreatedAtDesc(true, portfolioList);
        List<String> encodedImages = ImageEncoder.encode(portfolioList, imageItems);
        return FavoriteDTOConverter.findAllFavoritePortfolio(favoriteList, encodedImages);
    }

    private Portfolio findByPortfolioId(Long portfolioId) {
        return portfolioJPARepository.findById(portfolioId).orElseThrow(
                () -> new NotFoundException(BaseException.PORTFOLIO_NOT_FOUND)
        );
    }

    private User findByUserId(Long userId){
        return userJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
    }
}
