package com.kakao.sunsuwedding.portfolio;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PortfolioRequest {
    public record AddDTO(
        @NotEmpty(message = "plannerName은 비어있으면 안됩니다.")
        String plannerName,

        @NotEmpty(message = "title은 비어있으면 안됩니다.")
        String title,

        @NotEmpty(message = "description은 비어있으면 안됩니다.")
        String description,

        @NotEmpty(message = "location은 비어있으면 안됩니다.")
        String location,

        @NotEmpty(message = "career는 비어있으면 안됩니다.")
        String career,

        @NotEmpty(message = "partnerCompany는 비어있으면 안됩니다.")
        String partnerCompany,

        @NotNull(message = "items는 비어있으면 안됩니다.")
        List<ItemDTO> items,

        @NotNull(message = "images는 비어있으면 안됩니다.")
        List<String> images
    ) {}

    public record UpdateDTO(
        @NotEmpty(message = "plannerName은 비어있으면 안됩니다.")
        String plannerName,

        @NotEmpty(message = "title은 비어있으면 안됩니다.")
        String title,

        @NotEmpty(message = "description은 비어있으면 안됩니다.")
        String description,

        @NotEmpty(message = "location은 비어있으면 안됩니다.")
        String location,

        @NotEmpty(message = "career는 비어있으면 안됩니다.")
        String career,

        @NotEmpty(message = "partnerCompany는 비어있으면 안됩니다.")
        String partnerCompany,

        @NotNull(message = "items는 비어있으면 안됩니다.")
        List<ItemDTO> items,

        @NotNull(message = "images는 비어있으면 안됩니다.")
        List<String> images
    ) {}

    public record ItemDTO(
        @NotEmpty(message = "itemTitle은 비어있으면 안됩니다.")
        String itemTitle,

        @NotEmpty(message = "itemPrice는 비어있으면 안됩니다.")
        Long itemPrice
    ) {}

}
