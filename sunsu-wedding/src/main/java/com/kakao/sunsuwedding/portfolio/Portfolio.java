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
@SQLDelete(sql = "UPDATE portfolio_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Table(name = "portfolio_tb")
@NamedEntityGraph(name = "PortfolioWithPlanner",
                  attributeNodes = @NamedAttributeNode("planner"))
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Planner planner;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String location;

    @Column
    private String career;

    @Column(name = "partner_company")
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

    @Column(name = "is_active", columnDefinition = "boolean default true")
    private boolean isActive = true;

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

    public void updateConfirmedPrices(Long contractCount, Long avgPrice, Long minPrice, Long maxPrice) {
        this.contractCount = contractCount;
        this.avgPrice = avgPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
