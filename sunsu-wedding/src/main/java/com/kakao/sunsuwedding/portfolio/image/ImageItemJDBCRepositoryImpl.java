package com.kakao.sunsuwedding.portfolio.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageItemJDBCRepositoryImpl implements ImageItemJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ImageItemJDBCRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertImageItems(List<ImageItem> imageItems) {
        String sql = "INSERT INTO imageitem_tb (portfolio_id, origin_file_name, file_path, file_size, thumbnail) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, imageItems, imageItems.size(),
                (ps, priceItem) -> {
                    ps.setLong(1, priceItem.getPortfolio().getId());
                    ps.setString(2, priceItem.getOriginFileName());
                    ps.setString(3, priceItem.getFilePath());
                    ps.setLong(4, priceItem.getFileSize());
                    ps.setBoolean(5, priceItem.isThumbnail());
                });

    }

}
