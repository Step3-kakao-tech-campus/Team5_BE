package com.kakao.sunsuwedding.quotation;

import java.util.List;

public class QuotationDTOConverter {
    public static List<QuotationResponse.QuotationDTO> toFindByMatchIdDTO(List<Quotation> quotations) {
        return quotations
                .stream()
                .map(quotation -> new QuotationResponse.QuotationDTO(
                        quotation.getId(), quotation.getTitle(), quotation.getPrice(), quotation.getCompany(), quotation.getDescription(), quotation.getStatus().toString(), quotation.getModifiedAt()
                ))
                .toList();
    }

    public static List<QuotationResponse.QuotationsCollectDTO> toFindByCoupleDTO(List<Quotation> quotations) {
        return quotations.stream()
                .map(quotation -> new QuotationResponse.QuotationsCollectDTO(
                        (quotation.getMatch().getPlanner() != null) ? quotation.getMatch().getPlanner().getUsername() : "탈퇴한 사용자",
                        quotation.getId(),quotation.getTitle(), quotation.getPrice(), quotation.getCompany(),
                        quotation.getDescription(), quotation.getStatus().toString(), quotation.getModifiedAt()
                )).toList();
    }

    public static List<QuotationResponse.QuotationsCollectDTO> toFindByPlannerDTO(List<Quotation> quotations) {
        return quotations.stream()
                .map(quotation -> new QuotationResponse.QuotationsCollectDTO(
                        (quotation.getMatch().getCouple() != null) ? quotation.getMatch().getCouple().getUsername() : "탈퇴한 사용자",
                        quotation.getId(),quotation.getTitle(), quotation.getPrice(), quotation.getCompany(),
                        quotation.getDescription(), quotation.getStatus().toString(), quotation.getModifiedAt()
                )).toList();
    }
}
