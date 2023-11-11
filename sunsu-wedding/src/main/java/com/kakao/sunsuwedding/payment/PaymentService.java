package com.kakao.sunsuwedding.payment;

public interface PaymentService {

    void save(Long userId, PaymentRequest.SaveDTO requestDTO);

    void approve(Long userId, PaymentRequest.ApproveDTO requestDTO);
}
