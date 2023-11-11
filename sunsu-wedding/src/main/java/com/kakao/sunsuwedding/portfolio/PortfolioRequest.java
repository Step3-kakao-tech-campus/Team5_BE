package com.kakao.sunsuwedding.portfolio;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class PortfolioRequest {
    public record AddDTO(
        @NotEmpty(message = "포트폴리오 플래너 이름은 비어있으면 안됩니다.")
        @Size(max=10, message = "포트폴리오 플래너 이름은 최대 10자까지 입력 가능합니다.")
        String plannerName,

        @NotEmpty(message = "포트폴리오 한줄 소개는 비어있으면 안됩니다.")
        @Size(max=100, message = "포트폴리오 한줄 소개는 최대 100자까지 입력 가능합니다.")
        String title,

        @NotEmpty(message = "포트폴리오 상세 소개는 비어있으면 안됩니다.")
        @Size(max=1000, message = "포트폴리오 한줄 소개는 최대 1000자까지 입력 가능합니다.")
        String description,

        @NotEmpty(message = "포트폴리오 위치는 비어있으면 안됩니다.")
        @Size(max=255, message = "포트폴리오 위치는 최대 255자까지 입력 가능합니다.")
        String location,

        @NotEmpty(message = "포트폴리오 경력은 비어있으면 안됩니다.")
        @Size(max=1000, message = "포트폴리오 경력은 최대 1000자까지 입력 가능합니다.")
        String career,

        @NotEmpty(message = "포트폴리오 제휴 업체는 비어있으면 안됩니다.")
        @Size(max=1000, message = "포트폴리오 제휴 업체는 최대 1000자까지 입력 가능합니다.")
        String partnerCompany,

        @NotNull(message = "포트폴리오 가격 리스트는 비어있으면 안됩니다.")
        List<PriceItemDTO> items,

        @NotNull(message = "포트폴리오 이미지는 비어있으면 안됩니다.")
        List<String> images
    ) {}

    public record UpdateDTO(
        @NotEmpty(message = "포트폴리오 플래너 이름은 비어있으면 안됩니다.")
        @Size(max=10, message = "포트폴리오 플래너 이름은 최대 10자까지 입력 가능합니다.")
        String plannerName,

        @NotEmpty(message = "포트폴리오 한줄 소개는 비어있으면 안됩니다.")
        @Size(max=100, message = "포트폴리오 한줄 소개는 최대 100자까지 입력 가능합니다.")
        String title,

        @NotEmpty(message = "포트폴리오 상세 소개는 비어있으면 안됩니다.")
        @Size(max=1000, message = "포트폴리오 한줄 소개는 최대 1000자까지 입력 가능합니다.")
        String description,

        @NotEmpty(message = "포트폴리오 위치는 비어있으면 안됩니다.")
        @Size(max=255, message = "포트폴리오 위치는 최대 255자까지 입력 가능합니다.")
        String location,

        @NotEmpty(message = "포트폴리오 경력은 비어있으면 안됩니다.")
        @Size(max=1000, message = "포트폴리오 경력은 최대 1000자까지 입력 가능합니다.")
        String career,

        @NotEmpty(message = "포트폴리오 제휴 업체는 비어있으면 안됩니다.")
        @Size(max=1000, message = "포트폴리오 제휴 업체는 최대 1000자까지 입력 가능합니다.")
        String partnerCompany,

        @NotNull(message = "포트폴리오 가격 리스트는 비어있으면 안됩니다.")
        List<PriceItemDTO> items,

        @NotNull(message = "포트폴리오 이미지는 비어있으면 안됩니다.")
        List<String> images
    ) {}

    public record PriceItemDTO(
        @NotEmpty(message = "포트폴리오 가격의 제목은 비어있으면 안됩니다.")
        @Size(max=255, message = "포트폴리오 가격의 제목은 최대 255자까지 입력 가능합니다.")
        String itemTitle,

        @NotEmpty(message = "포트폴리오 가격은 비어있으면 안됩니다.")
        @Min(value = 0, message = "포트폴리오 가격은 양수여야 합니다.")
        Long itemPrice
    ) {}

}
