package com.ssafy.core.common;

import com.ssafy.core.exception.CustomException;

import static com.ssafy.core.exception.ErrorCode.INVALID_REQUEST;

public class KoreanUtil {
    public static String getCompleteWord(String str, String firstVal, String secondVal) {

        char laststr = str.charAt(str.length() - 1);
        // 한글의 제일 처음과 끝의 범위밖일 경우는 오류
        if (laststr < 0xAC00 || laststr > 0xD7A3) {
            throw new CustomException(INVALID_REQUEST);
        }

        int lastCharIndex = (laststr - 0xAC00) % 28;

        // 종성인덱스가 0이상일 경우는 받침이 있는경우이며 그렇지 않은경우는 받침이 없는 경우
        //받침이 있으면 first
        if (lastCharIndex > 0) {
            str += firstVal;
        } else {
            // 받침이 없는 경우
            str += secondVal;
        }

        return str;
    }
}
