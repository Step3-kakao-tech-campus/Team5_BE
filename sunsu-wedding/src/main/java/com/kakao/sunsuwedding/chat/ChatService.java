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

    /*
    @Transactional
    public void deleteChat(Pair<String, Long> info, Long matchId) {
        Match match = chatJPARepository.findById(matchId).orElseThrow(
                () -> new NotFoundException(BaseException.MATCHING_NOT_FOUND));

        // 유저 본인의 채팅방이 맞는지 확인
        permissionCheck(info, match);

        List<Quotation> quotations = chatJPARepository.findAllByMatch(match);
        // 견적서 존재하는데 전체 확정이 되지 않은 경우, 채팅방 삭제 불가
        if ((!quotations.isEmpty()) && (match.getStatus().equals(MatchStatus.UNCONFIRMED))) {
            throw new BadRequestException(BaseException.QUOTATION_NOT_CONFIRMED_ALL);
        }
        // 전체확정 됐거나, 견적서가 없는 경우 채팅방 삭제
        chatJPARepository.delete(match);
    }
     */

}
