package com.kakao.sunsuwedding.portfolio.image;

import com.kakao.sunsuwedding.portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "imageitem_tb")
@NoArgsConstructor
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
@Getter
public class ImageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private boolean thumbnail;

    @Builder
    public ImageItem(Long id, Portfolio portfolio, String originFileName, String filePath, Long fileSize, boolean thumbnail) {
        this.id = id;
        this.portfolio = portfolio;
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.thumbnail = thumbnail;
    }
}
