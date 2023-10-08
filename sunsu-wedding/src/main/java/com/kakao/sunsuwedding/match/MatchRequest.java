package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class MatchRequest {

    @Getter @Setter
    public static class AddMatchDTO {
        @NotNull(message = "plannerId는 비어있으면 안됩니다.")
        @Min(0)
        Long plannerId;

        public Match toMatchEntity(Couple couple, Planner planner){
            return Match.builder()
                    .couple(couple)
                    .planner(planner)
                    .price(0L)
                    .build();

        }
    }
}
