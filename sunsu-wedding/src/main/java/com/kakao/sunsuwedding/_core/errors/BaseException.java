package com.kakao.sunsuwedding._core.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 모든 에러의 메시지를 관리
@RequiredArgsConstructor
public enum BaseException {
    USER_NOT_FOUND("서비스를 탈퇴했거나 가입하지 않은 유저의 요청입니다.", 404),
    ACCESS_TOKEN_EXPIRED("access-token이 만료되었습니다. refresh-token 으로 다시 요청해주세요.", 403),
    ALL_TOKEN_EXPIRED("모든 토큰이 만료되었습니다. 다시 로그인 해야합니다.", 401),
    TOKEN_NOT_FOUND("토큰을 찾을 수 없습니다.", 404),
    TOKEN_NOT_VALID("로그인 토큰이 유효하지 않습니다. 다시 로그인 해주세요", 403),
    INVALID_TOKEN_ACCESS_DETECTED("올바르지 않은 접근입니다. 다시 로그인 해주세요.", 403),
    TOKEN_REFRESH_FORBIDDEN("토큰을 갱신할 수 없습니다.", 403),
    USER_EMAIL_EXIST("동일한 이메일이 존재합니다.", 400),
    USER_EMAIL_NOT_FOUND("이메일을 찾을 수 없습니다 : ", 400),
    USER_ROLE_WRONG("role은 플래너, 또는 예비 부부만 가능합니다.", 400),
    USER_PASSWORD_WRONG("패스워드를 잘못 입력하셨습니다",400),
    USER_PASSWORD_NOT_SAME("패스워드1과 패스워드2는 동일해야 합니다.: ", 400),
    USER_ALREADY_PREMIUM("이미 프리미엄 회원입니다.", 400),
    USER_TOKEN_WRONG("잘못된 토큰입니다", 401),
    PAYMENT_WRONG_INFORMATION("잘못된 결제 정보입니다.", 404),
    USER_UNEXPECTED_ERROR("[User] 예상치 못한 문제가 발생했습니다.", 500),
    PORTFOLIO_NOT_FOUND("해당하는 플래너의 포트폴리오가 삭제되었거나 존재하지 않습니다.", 404),
    PORTFOLIO_ALREADY_EXIST("해당 플래너의 포트폴리오가 이미 존재합니다. 포트폴리오는 플래너당 하나만 생성할 수 있습니다.", 400),
    PORTFOLIO_IMAGE_NOT_FOUND("포트폴리오 이미지를 불러올 수 없습니다.", 404),
    PORTFOLIO_IMAGE_CREATE_ERROR("포트폴리오 이미지 생성 과정에서 오류가 발생했습니다.", 500),
    PORTFOLIO_IMAGE_ENCODING_ERROR("이미지 인코딩 과정에서 오류가 발생했습니다.", 500),
    PORTFOLIO_CREATE_DIRECTORY_ERROR("포트폴리오 폴더 생성 과정에서 오류가 발생했습니다.", 500),
    PORTFOLIO_CLEAN_DIRECTORY_ERROR("포트폴리오 폴더를 비우는 과정에서 오류가 발생했습니다.", 500),
    PERMISSION_DENIED_METHOD_ACCESS("사용할 수 없는 기능입니다.", 403),
    DATABASE_ERROR("데이터베이스 에러입니다", 500),
    QUOTATIONS_NOT_ALL_CONFIRMED("확정되지 않은 견적서가 있습니다.",400),
    NO_QUOTATION_TO_CONFIRM("확정할 견적서가 없습니다",400),
    NOT_CONFIRMED_ALL_QUOTATIONS("견적서 전체 확정을 해야 합니다.", 400),
    MATCHING_NOT_FOUND("매칭 내역을 찾을 수 없습니다.", 404),
    QUOTATION_NOT_FOUND("해당 견적서를 찾을 수 없습니다.", 404),
    QUOTATION_CHANGE_DENIED("견적서가 이미 확정되어 수정할 수 없습니다.", 403),
    QUOTATION_ACCESS_DENIED("해당 매칭 내역에 접근할 수 없습니다.", 403),
    QUOTATION_ALREADY_CONFIRMED("견적서가 확정된 상태입니다.", 403),
    MATCHING_ALREADY_CONFIRMED("전체 확정되어 견적서를 추가할 수 없습니다.", 403);

    @Getter
    private final String message;

    @Getter
    private final int status;
}
