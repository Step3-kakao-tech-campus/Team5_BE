package com.kakao.sunsuwedding.portfolio.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PriceItemJDBCRepositoryImpl implements PriceItemJDBCRepository {
    private final JdbcTemplate jdbcTemplate;
    private final static String TABLE = "price_item_tb";

    @Autowired
    public PriceItemJDBCRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void batchInsertPriceItems(List<PriceItem> priceItems) {
        String sql = String.format("""
                INSERT INTO %s (portfolio_id, item_title, item_price)
                VALUES (?, ?, ?)
                """, TABLE);

        jdbcTemplate.batchUpdate(sql, priceItems, priceItems.size(),
                (ps, priceItem) -> {
                    ps.setLong(1, priceItem.getPortfolio().getId());
                    ps.setString(2, priceItem.getItemTitle());
                    ps.setLong(3, priceItem.getItemPrice());
                });
    }
}
