package com.kakao.sunsuwedding.match.Quotation;

import com.kakao.sunsuwedding.match.Match;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="quotation_tb")
@NamedEntityGraph(name = "QuotationWithMatch",
                  attributeNodes = @NamedAttributeNode("match"))
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
    private Boolean is_active;

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
        this.is_active = true;
    }

    public void updateTitle(String title) {
        this.title = title;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updatePrice(long price) {
        this.price = price;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateCompany(String company) {
        this.company = company;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateStatus(QuotationStatus status) {
        this.status = status;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateIsActive(Boolean is_active) {
        this.is_active = is_active;
        this.modifiedAt = LocalDateTime.now();
    }

    public void update(QuotationRequest.Update request) {
        this.title = request.title();
        this.price = request.price();
        this.company = request.company();
        this.description = request.description();
        this.modifiedAt = LocalDateTime.now();
    }
}
