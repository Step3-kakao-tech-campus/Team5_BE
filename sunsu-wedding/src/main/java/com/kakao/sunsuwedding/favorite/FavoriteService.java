package com.kakao.sunsuwedding.favorite;

import com.kakao.sunsuwedding.user.base_user.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteService {

    void likePortfolio(User user, Long portfolioId);

    void unlikePortfolio(User user, Long portfolioId);

    List<FavoriteResponse.FindPortfolioDTO> findFavoritePortfoliosByUser(User user, Pageable pageable);
}
