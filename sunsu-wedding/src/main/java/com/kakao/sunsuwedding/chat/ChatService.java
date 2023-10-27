package com.kakao.sunsuwedding.chat;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.match.MatchService;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatJPARepository chatJPARepository;
    private final CoupleJPARepository coupleJPARepository;
    private final PlannerJPARepository plannerJPARepository;

    private final MatchService matchService;

    @Transactional
    public ChatResponse.ChatDTO addChat(Pair<String, Long> user, ChatRequest.AddChatDTO requestDTO) {
        Long coupleId = user.getSecond();
        Long plannerId = requestDTO.plannerId();

        Couple couple = coupleJPARepository.findById(coupleId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND));
        Planner planner = plannerJPARepository.findById(plannerId).orElseThrow(
                () -> new NotFoundException(BaseException.PLANNER_NOT_FOUND));

        Chat chat = chatJPARepository.save(Chat.builder().build());

        // 채팅방 생성 시 매칭내역도 생성
        matchService.addMatch(couple, planner, chat);

        return new ChatResponse.ChatDTO(chat.getId());
    }
}
