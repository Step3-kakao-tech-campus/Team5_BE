package com.kakao.sunsuwedding.user.base_user;

import com.kakao.sunsuwedding.user.constant.Grade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;


@Entity
@Table(
        name="user_tb",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id", "email", "is_active"})
        },
        indexes = {
                @Index(name = "email_index", columnList = "email")
        })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@SQLDelete(sql = "UPDATE user_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name = "upgrade_at")
    private LocalDateTime upgradeAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    // 유저 등급 업그레이드
    public void upgrade() {
        this.grade = Grade.PREMIUM;
        this.upgradeAt = LocalDateTime.now();
    }

    // planner인지 couple인지 @DiscriminatorColumn의 내용을 보고 알려줌
    @Transient
    public String getDtype(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );
        return val == null ? null : val.value();
    }
}