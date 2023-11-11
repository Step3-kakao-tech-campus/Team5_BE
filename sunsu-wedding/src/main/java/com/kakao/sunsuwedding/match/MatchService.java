package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.chat.Chat;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import org.springframework.data.util.Pair;

public interface MatchService {

    Pair<Boolean, Long> addMatch(Couple couple, Planner planner, Chat chat);

    MatchResponse.FindAllWithNoReviewDTO findMatchesWithNoReview(User user);

    void confirm(User user, Long chatId);
}
