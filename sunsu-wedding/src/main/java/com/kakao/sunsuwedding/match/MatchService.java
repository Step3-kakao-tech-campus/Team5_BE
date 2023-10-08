package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.match.Quotation.Quotation;
import com.kakao.sunsuwedding.match.Quotation.QuotationJPARepository;
import com.kakao.sunsuwedding.match.Quotation.QuotationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MatchService {
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;

    // Match Update : 확정 상태, 가격, 확정 날짜
    @Transactional
    public void confirmAll(Long matchId) {
        Match match = matchJPARepository.findById(matchId).orElseThrow(
                () -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));
        // 플래너가 1개씩 전부 확정한 후에 예비 부부가 전체 확정 가능
        Pair<Boolean, Long> result = isAllConfirmed(match);
        // 모든 견적서 확정 완료 시
        if (result.getFirst()) {
            match.updateStatus(MatchStatus.CONFIRMED);
            match.updatePrice(result.getSecond());
            match.updateConfirmedAt(LocalDateTime.now());
        }
        // 확정되지 않은 견적서가 있을 때
        else {
            throw new Exception400(BaseException.QUOTATIONS_NOT_ALL_CONFIRMED.getMessage());
        }
    }

    // Match Delete : isActive 필드 false로
    // 채팅방 삭제 - 전체 확정 후 / 견적서 없을 때
    @Transactional
    public void deleteChat(Long matchId) {
        Match match = matchJPARepository.findById(matchId).orElseThrow(
                () -> new Exception404(BaseException.MATCHING_NOT_FOUND.getMessage()));
        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        // 견적서 존재하는데 전체 확정이 되지 않은 경우, 채팅방 삭제 불가
        if ((!quotations.isEmpty()) && (match.getStatus().equals(MatchStatus.UNCONFIRMED))) {
            throw new Exception400(BaseException.QUOTATIONS_NOT_ALL_CONFIRMED.getMessage());
        }
        // 전체확정 됐거나, 견적서가 없는 경우 채팅방 삭제
        match.updateIsActive(false);
    }

    private Pair<Boolean, Long> isAllConfirmed(Match match) {
        List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
        if (quotations.isEmpty()) {
            throw new Exception400(BaseException.NO_QUOTATION_TO_CONFIRM.getMessage());
        }
        else {
            // 모든 견적서 확정 됐는지 여부 구하기
            Boolean allConfirmed = quotations.stream().allMatch(quotation -> quotation.getStatus().equals(QuotationStatus.CONFIRMED));
            quotations.stream().forEach(quotation -> System.out.println(quotation.getStatus()));

            // Total Price 구하기
            Long totalPrice = quotations.stream().mapToLong(Quotation::getPrice).sum();

            return Pair.of(allConfirmed, totalPrice);
        }
    }
}