package com.kakao.sunsuwedding.favorite;

import java.util.List;
import java.util.stream.IntStream;

public class FavoriteDTOConverter {

    public static List<FavoriteResponse.FindPortfolioDTO> findAllFavoritePortfolio(List<Favorite> favorites) {
        return IntStream
                .range(0, favorites.size())
                .mapToObj(i -> {
                    Favorite favorite = favorites.get(i);
                    return new FavoriteResponse.FindPortfolioDTO(
                            favorite.getPortfolio().getId(),
                            "", // todo 찜하기 썸네일 이미지
                            favorite.getPortfolio().getTitle(),
                            favorite.getPortfolio().getPlanner().getUsername(),
                            favorite.getPortfolio().getTotalPrice(),
                            favorite.getPortfolio().getLocation(),
                            favorite.getPortfolio().getContractCount(),
                            true
                    );
                })
                .toList();
    }
}
