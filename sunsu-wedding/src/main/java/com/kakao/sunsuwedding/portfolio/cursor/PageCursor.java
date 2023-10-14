package com.kakao.sunsuwedding.portfolio.cursor;

public record PageCursor<T> (
        T data,
        Long cursor
) {
}
