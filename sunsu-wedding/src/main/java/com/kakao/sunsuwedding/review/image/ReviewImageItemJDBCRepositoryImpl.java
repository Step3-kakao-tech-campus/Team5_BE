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
        String sql = "INSERT INTO review_imageitem_tb (review_id, image, thumbnail) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, reviewImageItems, reviewImageItems.size(),
                (ps, imageItem) -> {
                    ps.setLong(1, imageItem.getReview().getId());
                    ps.setString(2, imageItem.getImage());
                    ps.setBoolean(3, imageItem.getThumbnail());
                });

    }
}

