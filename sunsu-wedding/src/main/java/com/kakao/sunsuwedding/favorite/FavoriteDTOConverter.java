package com.kakao.sunsuwedding.favorite;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class FavoriteDTOConverter {

    public List<FavoriteResponse.FindPortfolioDTO> findAllFavoritePortfolio(List<Favorite> favorites, List<String> images) {
        return IntStream
                .range(0, favorites.size())
                .mapToObj(i -> {
                    Favorite favorite = favorites.get(i);
                    return new FavoriteResponse.FindPortfolioDTO(
                            favorite.getPortfolio().getId(),
                            images.get(i),
                            favorite.getPortfolio().getTitle(),
                            favorite.getPortfolio().getPlannerName(),
                            favorite.getPortfolio().getTotalPrice(),
                            favorite.getPortfolio().getLocation(),
                            favorite.getPortfolio().getContractCount(),
                            favorite.getPortfolio().getAvgStars(),
                            true
                    );
                })
                .toList();
    }
}
