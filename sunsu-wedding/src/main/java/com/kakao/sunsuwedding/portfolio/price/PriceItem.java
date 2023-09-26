package com.kakao.sunsuwedding.portfolio.price;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PriceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Portfolio portfolio;

    private String itemTitle;
    private Long itemPrice;

}
