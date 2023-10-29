package com.kakao.sunsuwedding.match;

import java.util.List;

public class MatchDTOConverter {
    public static List<MatchResponse.MatchDTO> toMatchesWithNoReviewDTO(List<Match> matches) {
        return matches.stream()
                .map(match -> new MatchResponse.MatchDTO(match.getChat().getId(), match.getPlanner().getUsername()))
                .toList();
    }
}
