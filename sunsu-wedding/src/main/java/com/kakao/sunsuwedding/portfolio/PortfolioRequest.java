package com.kakao.sunsuwedding.portfolio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class PortfolioRequest {
    @Getter @Setter @ToString
    public static class addDTO {
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
    public static class updateDTO {
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
