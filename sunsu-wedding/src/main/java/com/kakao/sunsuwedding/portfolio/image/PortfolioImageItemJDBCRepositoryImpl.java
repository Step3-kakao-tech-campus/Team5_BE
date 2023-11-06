package com.kakao.sunsuwedding.portfolio.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PortfolioImageItemJDBCRepositoryImpl implements PortfolioImageItemJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PortfolioImageItemJDBCRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertImageItems(List<PortfolioImageItem> portfolioImageItems) {
        String sql = "INSERT INTO portfolioimageitem_tb (portfolio_id, image, thumbnail) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, portfolioImageItems, portfolioImageItems.size(),
                (ps, imageItem) -> {
                    ps.setLong(1, imageItem.getPortfolio().getId());
                    ps.setString(2, imageItem.getImage());
                    ps.setBoolean(3, imageItem.getThumbnail());
                });

    }

}
