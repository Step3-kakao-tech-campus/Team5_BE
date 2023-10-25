package com.kakao.sunsuwedding.portfolio;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class PortfolioRequest {
    @Getter
    @Setter
    @ToString
    public static class AddDTO {
        @NotNull(message = "plannerName은 비어있으면 안됩니다.")
        private String plannerName;

        @NotNull(message = "title은 비어있으면 안됩니다.")
        private String title;

        @NotNull(message = "description은 비어있으면 안됩니다.")
        private String description;

        @NotNull(message = "location은 비어있으면 안됩니다.")
        private String location;

        @NotNull(message = "career는 비어있으면 안됩니다.")
        private String career;

        @NotNull(message = "partnerCompany는 비어있으면 안됩니다.")
        private String partnerCompany;

        private List<ItemDTO> items;



    }

    @Getter
    @Setter
    @ToString
    public static class UpdateDTO {
        @NotNull(message = "plannerName은 비어있으면 안됩니다.")
        private String plannerName;

        @NotNull(message = "title은 비어있으면 안됩니다.")
        private String title;

        @NotNull(message = "description은 비어있으면 안됩니다.")
        private String description;

        @NotNull(message = "location은 비어있으면 안됩니다.")
        private String location;

        @NotNull(message = "career는 비어있으면 안됩니다.")
        private String career;

        @NotNull(message = "partnerCompany는 비어있으면 안됩니다.")
        private String partnerCompany;

        private List<ItemDTO> items;
    }

    @Getter
    @Setter
    @ToString
    public static class ItemDTO {
        @NotNull(message = "itemTitle은 비어있으면 안됩니다.")
        private String itemTitle;

        @NotNull(message = "itemPrice는 비어있으면 안됩니다.")
        private Long itemPrice;
    }
}
