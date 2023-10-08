package com.kakao.sunsuwedding.user.payment;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final UserJPARepository userJPARepository;

    // 결제와 관련된 정보를 user에 저장함
    @Transactional
    public void save(Long userId, PaymentRequest.SaveDTO requestDTO){
        User user = findUserById(userId);
        user.savePaymentInfo(requestDTO.getOrderId(), requestDTO.getAmount());
    }

    // 검증에 성공하면 success, 실패하면 fail 을 반환
    public String confirm(Long userId, PaymentRequest.ConfirmDTO requestDTO){
        User user = findUserById(userId);
        boolean isOK = isCorrectData(user, requestDTO.getOrderId(), requestDTO.getAmount());

        return isOK ? "success" : "fail";
    }

    // 유저 등급을 NORMAL -> PREMIUM으로 업그레이드 시켜줌
    @Transactional
    public String upgrade(Long userId, PaymentRequest.UpgradeDTO requestDTO) {
        User user = findUserById(userId);
        boolean isOK = isCorrectData(user, requestDTO.getOrderId(), requestDTO.getAmount())
                && requestDTO.getStatus().equals("DONE");

        if (isOK) user.upgrade();
        return isOK ? "success" : "fail";
    }

    // 받아온 payment와 관련된 데이터(orderId, amount)가 정확한지 확인)
    private boolean isCorrectData(User user, String orderId, Long amount){
        return user.getOrder_id().equals(orderId)
                && Objects.equals(user.getPayed_amount(), amount);
    }

    private User findUserById(Long userId){
        User user = userJPARepository.findById(userId).orElseThrow(
                () -> new Exception404(BaseException.USER_NOT_FOUND.getMessage())
        );
        // 이미 프리미엄 등급인 경우 결제하면 안되므로 에러 던짐
        if (user.getGrade() == Grade.PREMIUM){
            throw new Exception400(BaseException.USER_ALREADY_PREMIUM.getMessage());
        }
        return user;
    }
}
