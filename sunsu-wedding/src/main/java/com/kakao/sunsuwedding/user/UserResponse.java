package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.utils.DateFormat;
import com.kakao.sunsuwedding.user.base_user.User;
import lombok.Getter;
import lombok.Setter;

public class UserResponse {

    public record FindById (
        Long userId,
        String username,
        String email,
        String role,
        String grade,
        String payedAt
    ) {}

    public record FindUserId (
        Long userId
    ) {}

}
