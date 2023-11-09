package com.kakao.sunsuwedding.user.email;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EmailRequest {
    public record SendCode(

            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
            String email

    ) {}

    public record CheckCode(

            @NotEmpty(message = "이메일은 비어있으면 안됩니다.")
            @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
            String email,

            @NotEmpty(message = "인증코드는 비어있으면 안됩니다.")
            @Size(min = 6, max = 6, message = "인증코드는 6글자입니다.")
            String code
    ) {}
}
