package com.kakao.sunsuwedding.match;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="match_tb")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Planner planner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Couple couple;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private long price;

    @Column
    private LocalDateTime confirmedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
