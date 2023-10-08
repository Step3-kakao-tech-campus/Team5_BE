package com.kakao.sunsuwedding.user.couple;

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
@DiscriminatorValue(value = "couple")
@SQLDelete(sql = "UPDATE user_tb SET is_active = false WHERE id = ?")
public class Couple extends User {

    @Builder
    public Couple(Long id, String email, String password, String username, Grade grade, boolean is_active, LocalDateTime created_at, String order_id, Long payed_amount, LocalDateTime payed_at) {
        super(id, email, password, username, grade, is_active, created_at, order_id, payed_amount, payed_at);
    }
}