package com.kakao.sunsuwedding.portfolio;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PortfolioRequest {
    public record AddDTO(
        @NotNull(message = "plannerName은 비어있으면 안됩니다.")
        String plannerName,

        @NotNull(message = "title은 비어있으면 안됩니다.")
        String title,

        @NotNull(message = "description은 비어있으면 안됩니다.")
        String description,

        @NotNull(message = "location은 비어있으면 안됩니다.")
        String location,

        @NotNull(message = "career는 비어있으면 안됩니다.")
        String career,

        @NotNull(message = "partnerCompany는 비어있으면 안됩니다.")
        String partnerCompany,

        List<ItemDTO> items,

        List<String> images
    ) {}

    public record UpdateDTO(
        @NotNull(message = "plannerName은 비어있으면 안됩니다.")
        String plannerName,

        @NotNull(message = "title은 비어있으면 안됩니다.")
        String title,

        @NotNull(message = "description은 비어있으면 안됩니다.")
        String description,

        @NotNull(message = "location은 비어있으면 안됩니다.")
        String location,

        @NotNull(message = "career는 비어있으면 안됩니다.")
        String career,

        @NotNull(message = "partnerCompany는 비어있으면 안됩니다.")
        String partnerCompany,

        List<ItemDTO> items,

        List<String> images
    ) {}

    public record ItemDTO(
        @NotNull(message = "itemTitle은 비어있으면 안됩니다.")
        String itemTitle,

        @NotNull(message = "itemPrice는 비어있으면 안됩니다.")
        Long itemPrice
    ) {}

}
