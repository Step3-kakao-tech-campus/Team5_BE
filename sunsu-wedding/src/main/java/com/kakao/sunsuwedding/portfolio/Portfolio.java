package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "portfolio_tb")
@OnDelete(action= OnDeleteAction.CASCADE)
@SQLDelete(sql = "UPDATE portfolio_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "planner_id")
    private Planner planner;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String location;

    @Column
    private String career;

    @Column
    private String partnerCompany;

    @Column
    private Long totalPrice;

    @Column
    private Long contractCount;

    @Column
    private Long avgPrice;

    @Column
    private Long minPrice;

    @Column
    private Long maxPrice;

    @Column
    private LocalDateTime createdAt;

    @Column(columnDefinition = "boolean default true")
    private boolean is_active;

    @Builder
    public Portfolio(Long id, Planner planner, String title, String description, String location, String career, String partnerCompany, Long totalPrice, Long contractCount, Long avgPrice, Long minPrice, Long maxPrice, LocalDateTime createdAt) {
        this.id = id;
        this.planner = planner;
        this.title = title;
        this.description = description;
        this.location = location;
        this.career = career;
        this.partnerCompany = partnerCompany;
        this.totalPrice = totalPrice;
        this.contractCount = contractCount;
        this.avgPrice = avgPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.createdAt = (createdAt == null? LocalDateTime.now() : createdAt);
    }
}
