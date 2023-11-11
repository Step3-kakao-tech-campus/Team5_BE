package com.kakao.sunsuwedding.quotation;

import com.kakao.sunsuwedding.user.base_user.User;

public interface QuotationService {

    void addQuotation(User user, Long chatId, QuotationRequest.Add request);

    QuotationResponse.FindAllByMatchId findQuotationsByChatId(Long chatId);

    QuotationResponse.FindByUserDTO findQuotationsByUser(User user, int page);

    void confirm(User user, Long chatId, Long quotationId);

    void updateQuotation(User user, Long chatId, Long quotationId, QuotationRequest.Update request);

    void deleteQuotation(User user, Long quotationId);
}
