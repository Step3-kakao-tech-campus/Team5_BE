package com.kakao.sunsuwedding.portfolio.price;

import java.util.List;

public interface PriceItemJDBCRepository {
    void batchInsertPriceItems(List<PriceItem> priceItems);
}
