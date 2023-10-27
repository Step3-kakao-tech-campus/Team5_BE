package com.kakao.sunsuwedding.quotation;

import com.kakao.sunsuwedding.match.Match;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name="quotation_tb")
@NamedEntityGraph(name = "QuotationWithMatch",
                  attributeNodes = @NamedAttributeNode("match"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Match match;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long price;

    @Column
    private String company;

    @Column
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuotationStatus status;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;

    @Builder
    public Quotation(long id, Match match, String title, long price, String company, String description, QuotationStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.match = match;
        this.title = title;
        this.price = price;
        this.company = company;
        this.description = description;
        this.status = (status == null? QuotationStatus.UNCONFIRMED : status);
        this.createdAt = (createdAt == null? LocalDateTime.now() : createdAt);
        this.isActive = true;
    }

    public void updateTitle(String title) {
        this.title = title;
        updateModifiedAt();
    }

    public void updatePrice(long price) {
        this.price = price;
        updateModifiedAt();
    }

    public void updateCompany(String company) {
        this.company = company;
        updateModifiedAt();
    }

    public void updateDescription(String description) {
        this.description = description;
        updateModifiedAt();
    }

    public void updateStatus(QuotationStatus status) {
        this.status = status;
        updateModifiedAt();
    }

    public void update(QuotationRequest.Update request) {
        this.title = request.title();
        this.price = request.price();
        this.company = request.company();
        this.description = request.description();
        updateModifiedAt();
    }

    private void updateModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }
}
