package com.kakao.sunsuwedding.portfolio.price;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "priceitem_tb")
@NoArgsConstructor
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
@Getter
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

    public void updateItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }
    public void updateItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
    }
}
