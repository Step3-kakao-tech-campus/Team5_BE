package com.kakao.sunsuwedding._core.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 모든 에러의 메시지를 관리
@RequiredArgsConstructor
public enum BaseException {
    USER_NOT_FOUND("회원이 존재하지 않습니다.", 404),
    USER_EMAIL_EXIST("동일한 이메일이 존재합니다.", 400),
    USER_EMAIL_NOT_FOUND("이메일을 찾을 수 없습니다 : ", 400),
    USER_ROLE_WRONG("role은 플래너, 또는 예비 부부만 가능합니다.", 400),
    USER_PASSWORD_WRONG("패스워드를 잘못 입력하셨습니다",400),
    USER_PASSWORD_NOT_SAME("패스워드1과 패스워드2는 동일해야 합니다.: ", 400),
    USER_ALREADY_PREMIUM("이미 프리미엄 회원입니다.", 400),
    USER_UNEXPECTED_ERROR("[User] 예상치 못한 문제가 발생했습니다.", 500),
    PORTFOLIO_NOT_FOUND("포트폴리오를 찾을 수 없습니다.", 404),
    PORTFOLIO_IMAGE_NOT_FOUND("포트폴리오 이미지를 불러올 수 없습니다.", 404),
    PERMISSION_DENIED_METHOD_ACCESS("사용할 수 없는 기능입니다.", 403),
    DATABASE_ERROR("데이터베이스 에러입니다", 500),
    QUOTATIONS_NOT_ALL_CONFIRMED("확정되지 않은 견적서가 있습니다.",400),
    NO_QUOTATION_TO_CONFIRM("확정할 견적서가 없습니다",400),
    PAYMENT_HISTORY_NOT_FOUND("거래 내역이 없습니다.",404),
    MATCHING_NOT_FOUND("매칭 내역을 찾을 수 없습니다.", 400);

    @Getter
    private final String message;

    @Getter
    private final int status;
}
