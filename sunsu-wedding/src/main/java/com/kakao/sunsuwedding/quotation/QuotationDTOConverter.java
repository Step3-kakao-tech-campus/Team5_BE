package com.kakao.sunsuwedding.quotation;

import com.kakao.sunsuwedding._core.utils.DateFormat;
import com.kakao.sunsuwedding.user.constant.Role;

import java.util.*;

public class QuotationDTOConverter {
    public static List<QuotationResponse.QuotationDTO> toFindByMatchIdDTO(List<Quotation> quotations) {
        return quotations
                .stream()
                .map(quotation -> new QuotationResponse.QuotationDTO(
                        quotation.getId(), quotation.getTitle(), quotation.getPrice(), quotation.getCompany(),
                        quotation.getDescription(), quotation.getStatus().toString(), quotation.getModifiedAt()
                ))
                .toList();
    }

    public static List<QuotationResponse.QuotationsByChatIdDTO> toQuotationsByChatIdDTO(Map<Long, List<Quotation>> quotationsGroupByChatId,
                                                                                        List<Long> chatIds,
                                                                                        String role) {
        List<QuotationResponse.QuotationsByChatIdDTO> quotationsByChatIdDTOS = new ArrayList<>();

        for (Long chatId : chatIds) {
            List<Quotation> tempQuotations = quotationsGroupByChatId.get(chatId);
            List<QuotationResponse.QuotationsCollectDTO> quotationsCollectDTOS;
            String partnerName;
            String status = tempQuotations.get(0).getMatch().getStatus().getStatus();

            if (role.equals(Role.PLANNER.getRoleName())) {
                quotationsCollectDTOS = toFindByPlannerDTO(tempQuotations);
                partnerName = (tempQuotations.get(0).getMatch().getCouple() != null) ?
                        tempQuotations.get(0).getMatch().getCouple().getUsername() : "탈퇴한 사용자";
            }
            else {
                quotationsCollectDTOS = toFindByCoupleDTO(tempQuotations);
                partnerName = (tempQuotations.get(0).getMatch().getPlanner() != null) ?
                        tempQuotations.get(0).getMatch().getPlanner().getUsername() : "탈퇴한 사용자";
            }

            quotationsByChatIdDTOS.add(new QuotationResponse.QuotationsByChatIdDTO(chatId, partnerName,
                                                                                   status, quotationsCollectDTOS));
        }
        Collections.reverse(quotationsByChatIdDTOS);

        return quotationsByChatIdDTOS;
    }

    public static List<QuotationResponse.QuotationsCollectDTO> toFindByCoupleDTO(List<Quotation> quotations) {
        return quotations.stream()
                .map(quotation -> new QuotationResponse.QuotationsCollectDTO(quotation.getId(),
                        quotation.getTitle(), quotation.getPrice(), quotation.getCompany(),
                        quotation.getDescription(), quotation.getStatus().toString(),
                        DateFormat.dateFormatToMinuteKorean(quotation.getModifiedAt())
                )).toList();
    }

    public static List<QuotationResponse.QuotationsCollectDTO> toFindByPlannerDTO(List<Quotation> quotations) {
        return quotations.stream()
                .map(quotation -> new QuotationResponse.QuotationsCollectDTO(quotation.getId(),
                        quotation.getTitle(), quotation.getPrice(), quotation.getCompany(),
                        quotation.getDescription(), quotation.getStatus().toString(),
                        DateFormat.dateFormatToMinuteKorean(quotation.getModifiedAt())
                )).toList();
    }
}
