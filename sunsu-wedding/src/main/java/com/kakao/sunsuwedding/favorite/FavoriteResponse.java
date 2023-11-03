package com.kakao.sunsuwedding.favorite;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import com.kakao.sunsuwedding.portfolio.PortfolioResponse;
import lombok.Builder;

import java.util.List;
import java.util.stream.IntStream;

public class FavoriteResponse {
    public record FindPortfolioDTO(
            Long id,
            String image,
            String title,
            String plannerName,
            Long price,
            String location,
            Long contractCount,
            Boolean isLiked
    ) {
        @Builder
        public FindPortfolioDTO {

        }

        public static FindPortfolioDTO of(Favorite favorite){
            return FindPortfolioDTO.builder()
                    .id(favorite.getPortfolio().getId())
                    //.image(portfolio.)
                    .title(favorite.getPortfolio().getTitle())
                    .plannerName(favorite.getPortfolio().getPlanner().getUsername())
                    .price(favorite.getPortfolio().getTotalPrice())
                    .location(favorite.getPortfolio().getLocation())
                    .contractCount(favorite.getPortfolio().getContractCount())
                    .isLiked(true)
                    .build();
        }
    }
}
