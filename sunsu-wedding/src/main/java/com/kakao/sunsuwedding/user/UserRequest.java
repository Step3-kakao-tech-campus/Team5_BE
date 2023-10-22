package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserRequest {
    @Getter
    @Setter
    public static class SignUpDTO {

        @NotEmpty(message = "역할은 비어있으면 안됩니다.")
        private String role;

        @NotEmpty
        @Size(min = 2, max = 8, message = "2에서 8자 이내여야 합니다.")
        private String username;

        @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String password;

        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String password2;

        public Couple toCoupleEntity() {
            return Couple.builder()
                    .email(email)
                    .password(password)
                    .username(username)
                    .isActive(true)
                    .build();
        }
        public Planner toPlannerEntity() {
            return Planner.builder()
                    .email(email)
                    .password(password)
                    .username(username)
                    .isActive(true)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class LoginDTO {

        @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        @NotEmpty
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String password;
    }

    @Getter
    @Setter
    public static class EmailCheckDTO {
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;
    }
}
