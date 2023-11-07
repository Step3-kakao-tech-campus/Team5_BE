package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "portfolio_image_item_tb")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "ImageItemWithPortfolio",
                attributeNodes = @NamedAttributeNode("portfolio")
        ),
        @NamedEntityGraph(
                name = "ImageItemWithPortfolioAndPlanner",
                attributeNodes = @NamedAttributeNode(value = "portfolio", subgraph = "portfolioSubgraph"),
                subgraphs = @NamedSubgraph(name = "portfolioSubgraph", attributeNodes = @NamedAttributeNode("planner"))
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PortfolioImageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Portfolio portfolio;

    @Lob
    private String image;

    @Column(nullable = false)
    private Boolean thumbnail;

    @Builder
    public PortfolioImageItem(Long id, Portfolio portfolio, String image, Boolean thumbnail) {
        this.id = id;
        this.portfolio = portfolio;
        this.image = image;
        this.thumbnail = thumbnail;
    }
}
