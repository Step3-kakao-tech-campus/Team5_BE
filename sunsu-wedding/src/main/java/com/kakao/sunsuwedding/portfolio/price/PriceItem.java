package com.kakao.sunsuwedding.portfolio.price;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(
        name = "price_item_tb",
        indexes = {
                @Index(name = "portfolio_price_index", columnList = "portfolio_id")
        })
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PriceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Portfolio portfolio;

    @Column(name = "item_title", nullable = false)
    private String itemTitle;

    @Column(name = "item_price", nullable = false)
    private Long itemPrice;

    @Builder
    public PriceItem(Long id, Portfolio portfolio, String itemTitle, Long itemPrice) {
        this.id = id;
        this.portfolio = portfolio;
        this.itemTitle = itemTitle;
        this.itemPrice = itemPrice;
    }

}
