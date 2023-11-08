package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.chat.Chat;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;

public interface MatchService {

    void addMatch(Couple couple, Planner planner, Chat chat);

    MatchResponse.FindAllWithNoReviewDTO findMatchesWithNoReview(User user);

    void confirm(User user, Long chatId);
}
