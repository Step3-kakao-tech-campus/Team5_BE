package com.kakao.sunsuwedding.payment;

import com.kakao.sunsuwedding.user.base_user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE payment_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Table(name="payment_tb")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "payment_key", nullable = false)
    private String paymentKey;

    @Column(name = "payed_amount", nullable = false)
    private Long payedAmount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "payed_at")
    private LocalDateTime payedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public Payment(Long id, User user, String orderId, String paymentKey, Long payedAmount) {
        this.id = id;
        this.user = user;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.payedAmount = payedAmount;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public void updatePaymentInfo(String orderId, String paymentKey, Long amount){
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.payedAmount = amount;
    }

    public void updatePayedAt() {
        this.payedAt = LocalDateTime.now();
    }
}
