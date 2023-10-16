package com.kakao.sunsuwedding._core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormat {

    public static String dateFormatKorean(LocalDateTime time){
        if (time == null) return "";
        else return time.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }

}
