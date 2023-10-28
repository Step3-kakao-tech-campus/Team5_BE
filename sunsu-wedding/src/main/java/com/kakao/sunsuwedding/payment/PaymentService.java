package com.kakao.sunsuwedding.payment;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

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
            payment.updatePaymentInfo(requestDTO.getOrderId(), requestDTO.getAmount());
        }
        else {
            // 결제 정보 저장
            Payment payment = Payment.builder()
                    .user(user)
                    .orderId(requestDTO.getOrderId())
                    .payedAmount(requestDTO.getAmount())
                    .build();

            paymentJPARepository.save(payment);
        }
    }

    // 검증에 성공하면 success, 실패하면 fail 을 반환
    public String confirm(Long userId, PaymentRequest.ConfirmDTO requestDTO){
        User user = findUserById(userId);
        Payment payment = findPaymentByUserId(userId);
        boolean isOK = isCorrectData(payment, requestDTO.getOrderId(), requestDTO.getAmount());

        return isOK ? "success" : "fail";
    }

    // 유저 등급을 NORMAL -> PREMIUM으로 업그레이드 시켜줌
    @Transactional
    public void upgrade(Long userId, PaymentRequest.UpgradeDTO requestDTO) {
        User user = findUserById(userId);
        Payment payment = findPaymentByUserId(userId);

        boolean isOK = isCorrectData(payment, requestDTO.getOrderId(), requestDTO.getAmount())
                && requestDTO.getStatus().equals("DONE");

        if (isOK) {
            user.upgrade();
            payment.updatePayedAt();
        }
        else throw new BadRequestException(BaseException.PAYMENT_WRONG_INFORMATION);
    }

    @Transactional
    public void approve(Long userId, PaymentRequest.ApproveDTO requestDTO) {
        User user = findUserById(userId);
        Payment payment = findPaymentByUserId(user.getId());

        //  1. 검증: 프론트 정보와 백엔드 정보 비교
        boolean isOK = isCorrectData(payment, requestDTO.getOrderId(), requestDTO.getAmount());
        payment.updatePaymentKey(requestDTO.getPaymentKey());

        if (isOK){
            // 2. 토스 페이먼츠 승인 요청
            tossPayApprove(requestDTO);

            // 3. 유저 업그레이드
            user.upgrade();
            payment.updatePayedAt();
        }
        else throw new BadRequestException(BaseException.PAYMENT_WRONG_INFORMATION);
    }

    private void tossPayApprove(PaymentRequest.ApproveDTO requestDTO){
        // 토스페이먼츠 승인 api 요청
        // todo Authorization키 따로 환경 변수로 빼야 함
        String secretKey = "test_sk_GePWvyJnrKb2NW2jwE6VgLzN97Eo";
        String basicToken = "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        Map<String, String> parameters = new HashMap<>();
        parameters.put("paymentKey", requestDTO.getPaymentKey());
        parameters.put("orderId", requestDTO.getOrderId());
        parameters.put("amount", requestDTO.getAmount().toString());

        WebClient webClient =
                WebClient
                        .builder()
                        .baseUrl("https://api.tosspayments.com")
                        .build();

        TossPaymentResponse.TosspayDTO result =
                webClient
                        .post()
                        .uri("/v1/payments/confirm")
                        .headers(headers -> {
                            headers.add(HttpHeaders.AUTHORIZATION, basicToken);
                            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                        })
                        .bodyValue(parameters)
                        .retrieve()
                        .bodyToMono(TossPaymentResponse.TosspayDTO.class)
                        .block();
    }

    // 받아온 payment와 관련된 데이터(orderId, amount)가 정확한지 확인)
    private boolean isCorrectData(Payment payment, String orderId, Long amount){
        return payment.getOrderId().equals(orderId)
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
