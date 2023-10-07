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
    private int id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 256, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column
    private boolean is_active;

    @Column(nullable = false)
    private LocalDateTime created_at;

    // 결제와 관련된 필드
    @Column(length = 256)
    private String order_id;

    @Column
    private int payed_amount;

    @Column
    private LocalDateTime payed_at;

    // 유저 등급 업그레이드
    public void upgrade() {
        this.grade = Grade.PREMIUM;
        this.payed_at = LocalDateTime.now();
    }

    @Transient
    public String getDtype(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );
        return val == null ? null : val.value();
    }
}