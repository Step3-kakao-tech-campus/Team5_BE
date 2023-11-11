package com.kakao.sunsuwedding.favorite;

public class FavoriteResponse {
    public record FindPortfolioDTO(
            Long id,
            String image,
            String title,
            String plannerName,
            Long price,
            String location,
            Long contractCount,
            Double avgStars,
            Boolean isLiked
    ) {}
}
