package com.kakao.sunsuwedding.payment;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import com.kakao.sunsuwedding._core.errors.exception.NotFoundException;
import com.kakao.sunsuwedding._core.errors.exception.ServerException;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import com.kakao.sunsuwedding.user.constant.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentJPARepository paymentJPARepository;
    private final UserJPARepository userJPARepository;

    @Value("${payment.toss.secret}")
    private String secretKey;

    // 결제와 관련된 정보를 user에 저장함
    @Transactional
    public void save(Long userId, PaymentRequest.SaveDTO requestDTO){
        User user = findUserById(userId);
        Optional<Payment> paymentOptional = paymentJPARepository.findByUserId(userId);

        // 사용자의 결제 정보가 존재하면 업데이트
        if (paymentOptional.isPresent()){
            Payment payment = paymentOptional.get();
            payment.updatePaymentInfo(requestDTO.orderId(), requestDTO.amount());
        }
        else {
            // 결제 정보 저장
            Payment payment = Payment.builder()
                    .user(user)
                    .orderId(requestDTO.orderId())
                    .payedAmount(requestDTO.amount())
                    .build();
            paymentJPARepository.save(payment);
        }
    }

    @Transactional
    public void approve(Long userId, PaymentRequest.ApproveDTO requestDTO) {
        User user = findUserById(userId);
        Payment payment = findPaymentByUserId(user.getId());

        //  1. 검증: 프론트 정보와 백엔드 정보 비교
        Boolean isOK = isCorrectData(payment, requestDTO.orderId(), requestDTO.amount());

        if (!isOK) {
            throw new BadRequestException(BaseException.PAYMENT_WRONG_INFORMATION);
        }

        payment.updatePaymentKey(requestDTO.paymentKey());
        // 2. 토스 페이먼츠 승인 요청
        tossPayApprove(requestDTO);
        // 3. 유저 업그레이드
        user.upgrade();
        // 4. 결제시간 업데이트
        payment.updatePayedAt();
    }

    private void tossPayApprove(PaymentRequest.ApproveDTO requestDTO){
        // 토스페이먼츠 승인 api 요청
        String basicToken = "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        Map<String, String> parameters = new HashMap<>();
        parameters.put("paymentKey", requestDTO.paymentKey());
        parameters.put("orderId", requestDTO.orderId());
        parameters.put("amount", requestDTO.amount().toString());

        HttpClient httpClient = HttpClient.create()
                .proxy(it ->
                        it.type(ProxyProvider.Proxy.HTTP)
                                .host("http://krmp-proxy.9rum.cc")
                                .port(3128)
                )
                .proxyWithSystemProperties();

        WebClient webClient =
                WebClient
                        .builder()
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
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
                        .onErrorResume(e -> {
                            throw new ServerException(BaseException.PAYMENT_FAIL);
                        })
                        .block();
    }

    // 받아온 payment와 관련된 데이터(orderId, amount)가 정확한지 확인)
    private Boolean isCorrectData(Payment payment, String orderId, Long amount){
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
