package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserRequest {
    public record SignUpDTO (

        @NotEmpty(message = "역할은 비어있으면 안됩니다.")
        String role,

        @NotEmpty(message = "이름은 비어있으면 안됩니다.")
        @Size(min = 2, max = 8, message = "이름은 2에서 8자 이내여야 합니다.")
        String username,

        @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
        @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        String email,

        @NotEmpty(message = "패스워드는 비어있으면 안됩니다.")
        @Size(min = 8, max = 20, message = "패스워드는 8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        String password,

        @NotEmpty(message = "패스워드2는 비어있으면 안됩니다.")
        @Size(min = 8, max = 20, message = "패스워드는 8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        String password2
    ){
        public Couple toCoupleEntity(String encodedPassword) {
            return Couple.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .isActive(true)
                    .build();
        }
        public Planner toPlannerEntity(String encodedPassword) {
            return Planner.builder()
                    .email(email)
                    .password(encodedPassword)
                    .username(username)
                    .isActive(true)
                    .build();
        }
    }

    public record LoginDTO(

        @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        String email,

        @NotEmpty(message = "패스워드는 비어있으면 안됩니다.")
        @Size(min = 8, max = 20, message = "8에서 20자 이내여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        String password
    ){
    }

    public record EmailCheckDTO(
        @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
        @Size(max = 255, message = "이메일은 255자 이내여야 합니다.")
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        String email
    ){
    }
}
