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

@Entity
@DiscriminatorValue(value = "planner")
@SQLDelete(sql = "UPDATE user_tb SET is_active = false WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Planner extends User {

    @Builder
    public Planner(Long id, String email, String password, String username, LocalDateTime upgradeAt, Boolean isActive) {
        super(id, email, password, username, Grade.NORMAL, upgradeAt, isActive, LocalDateTime.now());
    }
}