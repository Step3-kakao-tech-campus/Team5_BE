package com.kakao.sunsuwedding.portfolio.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PortfolioImageItemJDBCRepositoryImpl implements PortfolioImageItemJDBCRepository {

    private final JdbcTemplate jdbcTemplate;
    private final static String TABLE = "portfolio_image_item_tb";

    @Autowired
    public PortfolioImageItemJDBCRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertImageItems(List<PortfolioImageItem> portfolioImageItems) {
        String sql = String.format("""
                INSERT INTO %s (portfolio_id, image, thumbnail)
                VALUES (?, ?, ?)
                """, TABLE);

        jdbcTemplate.batchUpdate(sql, portfolioImageItems, portfolioImageItems.size(),
                (ps, imageItem) -> {
                    ps.setLong(1, imageItem.getPortfolio().getId());
                    ps.setString(2, imageItem.getImage());
                    ps.setBoolean(3, imageItem.getThumbnail());
                });

    }

}
