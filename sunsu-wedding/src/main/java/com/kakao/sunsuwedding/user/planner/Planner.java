package com.kakao.sunsuwedding.user.planner;

import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.constant.Grade;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(value = "planner")
@SQLDelete(sql = "UPDATE user_tb SET is_active = false WHERE id = ?")
public class Planner extends User {

    @Builder
    public Planner(Long id, String email, String password, String username, Grade grade, boolean isActive, LocalDateTime createdAt, String orderId, Long payedAmount, LocalDateTime payedAt) {
        super(id, email, password, username, grade, isActive, createdAt, orderId, payedAmount, payedAt);
    }
}