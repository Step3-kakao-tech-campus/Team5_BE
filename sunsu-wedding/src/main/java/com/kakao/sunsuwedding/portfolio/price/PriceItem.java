package com.kakao.sunsuwedding.portfolio.price;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PriceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Portfolio portfolio;

    private String itemTitle;

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
