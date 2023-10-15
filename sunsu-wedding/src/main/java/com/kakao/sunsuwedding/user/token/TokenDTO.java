package com.kakao.sunsuwedding.user.token;

public record TokenDTO(
        String accessToken,
        String refreshToken
) {
}
