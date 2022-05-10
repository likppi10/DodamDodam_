package com.ssafy.core.common;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class MissionList {

    //0 -> 에게 , 1 -> 을,를
    public static String[][] common = {
            {
                    "사랑한다고 말해주세요",
                    "보고싶다고 말해주세요"
            },
            {
                    "따뜻하게 안아주세요!"
            },
    };

    public static String[][] mother = {

    };

    public static String[][] father = {

    };

    public static String[][] son = {

    };

    public static String[][] daughter = {

    };

    public static Map<String, String[][]> missionList = Map.of(
            "엄마", Stream.concat(Arrays.stream(common), Arrays.stream(mother)).toArray(String[][]::new),
            "아빠", Stream.concat(Arrays.stream(common), Arrays.stream(father)).toArray(String[][]::new),
            "아들", Stream.concat(Arrays.stream(common), Arrays.stream(son)).toArray(String[][]::new),
            "딸", Stream.concat(Arrays.stream(common), Arrays.stream(daughter)).toArray(String[][]::new)
    );
}
