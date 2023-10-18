package com.kakao.sunsuwedding.portfolio;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class PortfolioRequest {
    @Getter @Setter @ToString
    public static class AddDTO {
        private String plannerName;
        private String title;
        private String description;
        private String location;
        private String career;
        private String partnerCompany;

        private List<ItemDTO> items;

        @Getter @Setter @ToString
        public static class ItemDTO {
            private String itemTitle;
            private Long itemPrice;
        }

    }

    @Getter @Setter @ToString
    public static class UpdateDTO {
        private String plannerName;
        private String title;
        private String description;
        private String location;
        private String career;
        private String partnerCompany;

        private List<ItemDTO> items;

        @Getter @Setter @ToString
        public static class ItemDTO {
            private String itemTitle;
            private Long itemPrice;
        }

    }
}
