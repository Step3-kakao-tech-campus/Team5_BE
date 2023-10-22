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

    public static List<QuotationResponse.QuotationWithPartnerDTO> toFindByCoupleDTO(List<Quotation> quotations) {
        return quotations.stream()
                .map(quotation -> new QuotationResponse.QuotationWithPartnerDTO(quotation.getMatch().getPlanner().getUsername(),
                        quotation.getId(),quotation.getTitle(), quotation.getPrice(), quotation.getCompany(),
                        quotation.getDescription(), quotation.getStatus().toString(), quotation.getModifiedAt()
                )).toList();
    }

    public static List<QuotationResponse.QuotationWithPartnerDTO> toFindByPlannerDTO(List<Quotation> quotations) {
        return quotations.stream()
                .map(quotation -> new QuotationResponse.QuotationWithPartnerDTO(quotation.getMatch().getCouple().getUsername(),
                        quotation.getId(),quotation.getTitle(), quotation.getPrice(), quotation.getCompany(),
                        quotation.getDescription(), quotation.getStatus().toString(), quotation.getModifiedAt()
                )).toList();
    }
}
