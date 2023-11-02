package com.kakao.sunsuwedding.match;

import java.util.List;

public class MatchResponse {
    public record FindAllWithNoReviewDTO(
            List<MatchDTO> matches
    ) {}

    public record MatchDTO(
            Long chatId,
            Long plannerId,
            String plannerName,
            String confirmedAt
    ) {}
}