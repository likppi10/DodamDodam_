package com.ssafy.core.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Validate {

    public static final int USER_ID_MAX = 20;
    public static final int USER_ID_MIN = 4;
    public static final int USER_PWD_MAX = 20;
    public static final int USER_PWD_MIN = 8;
    public static final int USER_NAME_MIN = 1;
    public static final int USER_NAME_MAX = 10;

    private final int number;
}
