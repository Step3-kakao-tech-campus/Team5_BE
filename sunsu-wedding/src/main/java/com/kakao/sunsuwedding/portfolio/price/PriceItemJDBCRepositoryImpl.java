package com.kakao.sunsuwedding.portfolio.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class PriceItemJDBCRepositoryImpl implements PriceItemJDBCRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PriceItemJDBCRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void batchInsertPriceItems(List<PriceItem> priceItems) {
        String sql = "INSERT INTO priceitem_tb (portfolio_id, item_title, item_price) VALUES (?, ?, ?)";

        // 람다식으로 구현할 수도 있고
        jdbcTemplate.batchUpdate(sql, priceItems, priceItems.size(),
                (ps, priceItem) -> {
                    ps.setLong(1, priceItem.getPortfolio().getId());
                    ps.setString(2, priceItem.getItemTitle());
                    ps.setLong(3, priceItem.getItemPrice());
                });
    }

    public void batchUpdatePriceItems(List<PriceItem> priceItems) {
        String sql = "UPDATE priceitem_tb SET item_title = ?, item_price = ? WHERE id = ?";

        // 메서드 추가가 필요하다면 다음처럼 구현할 수도 있음 (성능 차이는 없음)
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PriceItem priceItem = priceItems.get(i);
                ps.setString(1, priceItem.getItemTitle());
                ps.setLong(2, priceItem.getItemPrice());
                ps.setLong(3, priceItem.getId());
            }

            @Override
            public int getBatchSize() {
                return priceItems.size();
            }
        });
    }

}
