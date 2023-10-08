package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

public class UserResponse {
    @Getter
    @Setter
    public static class FindById{
        private String username;
        private String email;
        private String role;
        private String grade;
        private String payedAt;

        public FindById(Couple couple) {
            this.username = couple.getUsername();
            this.email = couple.getEmail();
            this.role = Role.COUPLE.getRoleName();
            this.grade = couple.getGrade().getGradeName();
            this.payedAt = (couple.getPayedAt() == null) ? null : couple.getPayedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
        public FindById(Planner planner) {
            this.username = planner.getUsername();
            this.email = planner.getEmail();
            this.role = Role.PLANNER.getRoleName();
            this.grade = planner.getGrade().getGradeName();
            this.payedAt = (planner.getPayedAt() == null) ? null : planner.getPayedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }
}
