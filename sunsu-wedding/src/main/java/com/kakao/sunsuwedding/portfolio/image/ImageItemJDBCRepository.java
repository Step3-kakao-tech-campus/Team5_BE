package com.kakao.sunsuwedding.portfolio.image;

import java.util.List;

public interface ImageItemJDBCRepository {
    void batchInsertImageItems(List<ImageItem> imageItems);
}
