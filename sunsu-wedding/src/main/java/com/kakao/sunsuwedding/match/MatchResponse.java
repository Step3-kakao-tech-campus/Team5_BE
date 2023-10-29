package com.kakao.sunsuwedding.match;

import java.util.List;

public class MatchResponse {
    public record MatchesWithNoReviewDTO(
            List<MatchDTO> matches
    ) {}

    public record MatchDTO(
            Long id,
            String plannerName
    ) {}
}
