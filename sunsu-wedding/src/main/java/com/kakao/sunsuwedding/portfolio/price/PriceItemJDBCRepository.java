package com.kakao.sunsuwedding.portfolio.price;

import java.util.List;
public interface PriceItemJDBCRepository {

    // PriceItemJDBCRepositoryImpl에 구현되어 있음
    void batchInsertPriceItems(List<PriceItem> priceItems);
    void batchUpdatePriceItems(List<PriceItem> priceItems);
}
