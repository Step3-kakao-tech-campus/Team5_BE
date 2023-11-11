package com.kakao.sunsuwedding.chat;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.match.MatchServiceImpl;
import com.kakao.sunsuwedding.user.base_user.User;
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
public class ChatServiceImpl implements ChatService {
    private final ChatJPARepository chatJPARepository;
    private final CoupleJPARepository coupleJPARepository;
    private final PlannerJPARepository plannerJPARepository;

    private final MatchServiceImpl matchServiceImpl;

    @Transactional
    public ChatResponse.ChatDTO addChat(User user, ChatRequest.AddChatDTO requestDTO) {
        Long coupleId = user.getId();
        Long plannerId = requestDTO.plannerId();

        Couple couple = findCoupleById(coupleId);
        Planner planner = findPlannerById(plannerId);

        Chat chat = chatJPARepository.save(Chat.builder().build());

        // 채팅방 생성 시 매칭내역도 생성
        Pair<Boolean, Long> chatInfo = matchServiceImpl.addMatch(couple, planner, chat);

        return new ChatResponse.ChatDTO(chatInfo.getFirst(), chatInfo.getSecond());
    }

    private Planner findPlannerById(Long plannerId) {
        return plannerJPARepository.findById(plannerId).orElseThrow(
                () -> new NotFoundException(BaseException.PLANNER_NOT_FOUND));
    }

    private Couple findCoupleById(Long coupleId) {
        return coupleJPARepository.findById(coupleId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND));
    }
}
