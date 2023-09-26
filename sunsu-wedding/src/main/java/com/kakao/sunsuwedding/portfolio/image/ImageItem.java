package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "ImageItemWithPortfolio",
                attributeNodes = @NamedAttributeNode("portfolio")
        ),
        @NamedEntityGraph(
                name = "ImageItemWithPortfolioAndPlanner",
                attributeNodes = @NamedAttributeNode(value = "portfolio", subgraph = "portfolioSubgraph"),
                subgraphs = @NamedSubgraph(name = "portfolioSubgraph", attributeNodes = @NamedAttributeNode("planner"))
        )
})
@Entity
public class ImageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private boolean thumbnail;

    @Builder
    public ImageItem(Long id, Portfolio portfolio, String originalFileName, String filePath, Long fileSize, boolean thumbnail) {
        this.id = id;
        this.portfolio = portfolio;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.thumbnail = thumbnail;
    }

    protected ImageItem() {
    }


}
