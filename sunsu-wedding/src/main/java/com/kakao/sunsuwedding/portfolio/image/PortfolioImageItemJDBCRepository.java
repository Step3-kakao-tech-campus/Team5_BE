package com.kakao.sunsuwedding.portfolio.image;

import java.util.List;

public interface PortfolioImageItemJDBCRepository {

    // ImageItemJDBCRepositoryImpl에 구현되어 있음
    // 로직 상 이미지 처리에 batchUpdate는 필요없음 (batchInsert로 처리함)
    void batchInsertImageItems(List<PortfolioImageItem> portfolioImageItems);
}
