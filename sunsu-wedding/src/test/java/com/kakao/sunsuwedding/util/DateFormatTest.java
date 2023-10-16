package com.kakao.sunsuwedding.util;

import com.kakao.sunsuwedding._core.utils.DateFormat;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class DateFormatTest {

    @Test
    public void date_format_korean_test(){
        LocalDateTime time = LocalDateTime.of(2002,9,12, 12,12,12);
        String result = DateFormat.dateFormatKorean(time);
        assertThat(result).isEqualTo("2002년 09월 12일");
    }

    @Test
    public void date_format_null_test(){
        String result = DateFormat.dateFormatKorean(null);
        assertThat(result).isEqualTo("");

    }
}
