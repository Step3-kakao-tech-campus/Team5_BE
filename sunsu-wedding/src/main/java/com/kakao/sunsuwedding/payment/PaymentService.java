package com.kakao.sunsuwedding.payment;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentJPARepository paymentJPARepository;
    private final UserJPARepository userJPARepository;

    // 결제와 관련된 정보를 user에 저장함
    @Transactional
    public void save(Long userId, PaymentRequest.SaveDTO requestDTO){
        User user = findUserById(userId);
        Optional<Payment> paymentOptional = paymentJPARepository.findByUserId(userId);

        // 사용자의 결제 정보가 존재하면 업데이트
        if (paymentOptional.isPresent()){
            Payment payment = paymentOptional.get();
            payment.updatePaymentInfo(requestDTO.getOrderId(), requestDTO.getPaymentKey(), requestDTO.getAmount());
        }
        else {
            // 결제 정보 저장
            Payment payment = Payment.builder()
                    .user(user)
                    .orderId(requestDTO.getOrderId())
                    .paymentKey(requestDTO.getPaymentKey())
                    .payedAmount(requestDTO.getAmount())
                    .build();

            paymentJPARepository.save(payment);
        }
    }

    // 검증에 성공하면 success, 실패하면 fail 을 반환
    public String confirm(Long userId, PaymentRequest.ConfirmDTO requestDTO){
        User user = findUserById(userId);
        Payment payment = findPaymentByUserId(userId);
        boolean isOK = isCorrectData(payment, requestDTO.getOrderId(), requestDTO.getAmount(), requestDTO.getPaymentKey());

        return isOK ? "success" : "fail";
    }

    // 유저 등급을 NORMAL -> PREMIUM으로 업그레이드 시켜줌
    @Transactional
    public void upgrade(Long userId, PaymentRequest.UpgradeDTO requestDTO) {
        User user = findUserById(userId);
        Payment payment = findPaymentByUserId(userId);

        boolean isOK = isCorrectData(payment, requestDTO.getOrderId(), requestDTO.getAmount(), requestDTO.getPaymentKey())
                && requestDTO.getStatus().equals("DONE");

        if (isOK) {
            user.upgrade();
            payment.updatePayedAt();
        }
        else throw new BadRequestException(BaseException.PAYMENT_WRONG_INFORMATION);
    }

    // 받아온 payment와 관련된 데이터(orderId, paymentKey, amount)가 정확한지 확인)
    private boolean isCorrectData(Payment payment, String orderId, Long amount, String paymentKey){
        return payment.getOrderId().equals(orderId)
                && payment.getPaymentKey().equals(paymentKey)
                && Objects.equals(payment.getPayedAmount(), amount);
    }

    private Payment findPaymentByUserId(Long userId){
        return paymentJPARepository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException(BaseException.PAYMENT_NOT_FOUND)
        );
    }

    private User findUserById(Long userId){
        User user = userJPARepository.findById(userId).orElseThrow(
                () -> new NotFoundException(BaseException.USER_NOT_FOUND)
        );
        // 이미 프리미엄 등급인 경우 결제하면 안되므로 에러 던짐
        if (user.getGrade() == Grade.PREMIUM){
            throw new BadRequestException(BaseException.USER_ALREADY_PREMIUM);
        }
        return user;
    }
}
