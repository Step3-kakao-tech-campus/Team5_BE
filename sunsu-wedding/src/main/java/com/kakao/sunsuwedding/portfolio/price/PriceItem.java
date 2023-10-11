package com.kakao.sunsuwedding.portfolio.price;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "PriceItemWithPortfolio",
                attributeNodes = @NamedAttributeNode("portfolio")
        ),
        @NamedEntityGraph(
                name = "PriceItemWithPortfolioAndPlanner",
                attributeNodes = @NamedAttributeNode(value = "portfolio", subgraph = "portfolioSubgraph"),
                subgraphs = @NamedSubgraph(name = "portfolioSubgraph", attributeNodes = @NamedAttributeNode("planner"))
        )
})
@Table(name = "priceitem_tb")
public class PriceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String itemTitle;

    @Column(nullable = false)
    private Long itemPrice;

    @Builder
    public PriceItem(Long id, Portfolio portfolio, String itemTitle, Long itemPrice) {
        this.id = id;
        this.portfolio = portfolio;
        this.itemTitle = itemTitle;
        this.itemPrice = itemPrice;
    }

}
