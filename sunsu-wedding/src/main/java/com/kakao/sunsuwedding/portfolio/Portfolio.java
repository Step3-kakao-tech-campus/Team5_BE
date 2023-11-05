package com.kakao.sunsuwedding.portfolio;

import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "portfolio_tb")
@SQLDelete(sql = "UPDATE portfolio_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Planner planner;

    @Column(nullable = false)
    private String plannerName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String career;

    @Column(name = "partner_company", nullable = false)
    private String partnerCompany;

    @Column(name = "total_price")
    private Long totalPrice;

    @Column(name = "contract_count")
    private Long contractCount;

    @Column(name = "avg_price")
    private Long avgPrice;

    @Column(name = "min_price")
    private Long minPrice;

    @Column(name = "max_price")
    private Long maxPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public Portfolio(Long id, Planner planner, String plannerName, String title, String description, String location, String career, String partnerCompany, Long totalPrice, Long contractCount, Long avgPrice, Long minPrice, Long maxPrice) {
        this.id = id;
        this.planner = planner;
        this.plannerName = plannerName;
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
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    public void update(String plannerName, String title, String description, String location,
                       String career, String partnerCompany, Long totalPrice){
        this.plannerName = plannerName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.career = career;
        this.partnerCompany = partnerCompany;
        this.totalPrice = totalPrice;
    }

    public void updateConfirmedPrices(Long contractCount, Long avgPrice, Long minPrice, Long maxPrice) {
        this.contractCount = contractCount;
        this.avgPrice = avgPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
