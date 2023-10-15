package com.kakao.sunsuwedding.user.base_user;

import com.kakao.sunsuwedding.user.constant.Grade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Where(clause = "is_active = true")
@Table(name="user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 256, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    // 결제와 관련된 필드
    @Column(length = 256, name = "order_id")
    private String orderId;

    @Column(name = "payed_amount")
    private Long payedAmount;

    @Column(name = "payed_at")
    private LocalDateTime payedAt;

    // 유저 등급 업그레이드
    public void upgrade() {
        this.grade = Grade.PREMIUM;
        this.payedAt = LocalDateTime.now();
    }

    // 결제 정보 저장 (주문 번호, 금액)
    public void savePaymentInfo(String order_id, Long payed_amount){
        this.orderId = order_id;
        this.payedAmount = payed_amount;
    }

    // planner인지 couple인지 @DiscriminatorColumn의 내용을 보고 알려줌
    @Transient
    public String getDtype(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );
        return val == null ? null : val.value();
    }
}