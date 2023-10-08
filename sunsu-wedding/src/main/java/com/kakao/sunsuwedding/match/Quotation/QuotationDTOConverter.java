package com.kakao.sunsuwedding.match.Quotation;

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
}
