package com.kakao.sunsuwedding.review.image;

import com.kakao.sunsuwedding.review.image.ReviewImageItem;
import com.kakao.sunsuwedding.review.image.ReviewImageItemJDBCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReviewImageItemJDBCRepositoryImpl implements ReviewImageItemJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewImageItemJDBCRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsertImageItems(List<ReviewImageItem> reviewImageItems) {
        String sql = "INSERT INTO imageitem_tb (review_id, origin_file_name, file_path, file_size, thumbnail) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, reviewImageItems, reviewImageItems.size(),
                (ps, reviewImageItem) -> {
                    ps.setLong(1, reviewImageItem.getReview().getId());
                    ps.setString(2, reviewImageItem.getOriginFileName());
                    ps.setString(3, reviewImageItem.getFilePath());
                    ps.setLong(4, reviewImageItem.getFileSize());
                    ps.setBoolean(5, reviewImageItem.isThumbnail());
                });

    }
}

