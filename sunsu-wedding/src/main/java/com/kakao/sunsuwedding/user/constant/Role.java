package com.kakao.sunsuwedding.user.constant;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.BadRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Role {
    COUPLE("couple"),
    PLANNER("planner");

    @Getter
    private final String roleName;

    public static Role valueOfRole(String role) {
        return Arrays.stream(values())
                .filter(value -> value.roleName.equals(role))
                .findAny()
                .orElseThrow(() -> new BadRequestException(BaseException.USER_ROLE_WRONG));
    }
}
