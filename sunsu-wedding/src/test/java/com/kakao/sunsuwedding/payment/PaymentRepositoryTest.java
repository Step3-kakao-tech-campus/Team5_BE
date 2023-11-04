package com.kakao.sunsuwedding.payment;

import com.kakao.sunsuwedding._core.DummyEntity;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.MatchStatus;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PaymentRepositoryTest extends DummyEntity {

    @Autowired
    private PaymentJPARepository paymentJPARepository;

    @Autowired
    private CoupleJPARepository coupleJPARepository;

    @Autowired
    private PlannerJPARepository plannerJPARepository;

    private Long id1, id2;

    Couple couple;
    Planner planner;

    @BeforeEach
    void setUp() {
        couple = coupleJPARepository.save(newCouple("newcouple"));
        planner = plannerJPARepository.save(newPlanner("newplanner"));

        Payment m1 = newPayment(couple, "order1","payment1", 10000L);
        Payment m2 = newPayment(planner, "order2","payment2", 20000L);

        id1 = paymentJPARepository.save(m1).getId();
        id2 = paymentJPARepository.save(m2).getId();
    }

    @AfterEach
    void tearDown() {
        paymentJPARepository.deleteAll();
        coupleJPARepository.deleteAll();
        plannerJPARepository.deleteAll();
    }

    @Test
    @DisplayName("id로 매칭 찾기")
    void findPaymentById(){
        // when
        Payment payment = paymentJPARepository.findById(id1).orElseThrow(
                () -> new RuntimeException("결제 정보를 찾을 수 없습니다.")
        );

        // then
        assertThat(payment.getUser().getEmail()).isEqualTo("newcouple@nate.com");
        assertThat(payment.getOrderId()).isEqualTo("order1");
        assertThat(payment.getPaymentKey()).isEqualTo("payment1");
        assertThat(payment.getPayedAmount()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("사용자 id로 매칭 찾기")
    void findPaymentByUserId(){
        // when
        Payment payment = paymentJPARepository.findByUserId(planner.getId()).orElseThrow(
                () -> new RuntimeException("해당 유저의 결제 정보를 찾을 수 없습니다.")
        );

        // then
        assertThat(payment.getUser().getEmail()).isEqualTo("newplanner@nate.com");
        assertThat(payment.getOrderId()).isEqualTo("order2");
        assertThat(payment.getPaymentKey()).isEqualTo("payment2");
        assertThat(payment.getPayedAmount()).isEqualTo(20000L);
    }

    @Test
    @DisplayName("결제 정보 저장하기")
    void savePayment(){
        // given
        Couple couple = coupleJPARepository.save(newCouple("newcouple2"));
        Payment p1 = newPayment(couple, "order_test", "payment_test", 1000L);

        // when
        Payment payment = paymentJPARepository.save(p1);

        // then
        assertThat(payment.getUser().getEmail()).isEqualTo("newcouple2@nate.com");
        assertThat(payment.getOrderId()).isEqualTo("order_test");
        assertThat(payment.getPaymentKey()).isEqualTo("payment_test");
        assertThat(payment.getPayedAmount()).isEqualTo(1000L);
    }

}