package com.ssafy.core.common;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class MissionListUtil {

    //0 -> 에게 , 1 -> 을,를 . 2 -> 과, 와
    public static String[][] common = {
            {
                    "사랑한다고 말해주세요!",
                    "보고싶다고 말해주세요!",
                    "어제 아침에 무엇을 먹었는지 알려주세요!",
                    "어제 점심에 무엇을 먹었는지 알려주세요!",
                    "어제 저녁에 무엇을 먹었는지 알려주세요!",
                    "좋아하는 가수가 누구인지 물어봐주세요!",
                    "좋아하는 노래가 무엇인지 물어봐주세요!",
                    "어디로 여행을 가고 싶은지 물어봐주세요!",
                    "좋아하는 음식을 물어봐주세요!",
                    "아픈 곳은 없는지 물어봐주세요!",

            },
            {
                    "따뜻하게 안아주세요!",
                    "주변에 자랑해주세요!",
            },
            {
                    "찍은 사진을 앨범에 올려보세요!",
                    "주말 여행 계획을 짜보세요!",
                    "오늘 식사를 같이 해봐요!",
                    "같이 사진을 찍어보세요!",
            }
    };

    public static String[][] mother = {
            {
                    "꽃 한 송이 선물해 주세요!",
                    "언제나 감사하다고 말해주세요!",
                    "좋아하는 드라마가 무엇인지 물어봐주세요!",
            },
            {

            },
            {
                    "식사를 같이 준비해 봐요!",
            }
    };

    public static String[][] father = {
            {
                    "꽃 한 송이 선물해 주세요!",
                    "언제나 감사하다고 말해주세요!",
            },
            {

            },
            {
                    "식사를 같이 준비해 봐요!",
            }
    };

    public static String[][] son = {
            {
                    "어떤 게임을 좋아하는지 물어봐주세요!",
                    "좋아하는 과자가 무엇인지 물어봐주세요!",
                    "오늘 하루 어땠는지 물어봐주세요!",

            },
            {

            },
            {

            }
    };

    public static String[][] daughter = {
            {
                    "어떤 게임을 좋아하는지 물어봐주세요!",
                    "좋아하는 과자가 무엇인지 물어봐주세요!",
                    "오늘 하루 어땠는지 물어봐주세요!",
            },
            {

            },
            {

            }
    };

    public static Map<String, String[][]> missionList = Map.of(
            "엄마", Stream.concat(Arrays.stream(common), Arrays.stream(mother)).toArray(String[][]::new),
            "아빠", Stream.concat(Arrays.stream(common), Arrays.stream(father)).toArray(String[][]::new),
            "아들", Stream.concat(Arrays.stream(common), Arrays.stream(son)).toArray(String[][]::new),
            "딸", Stream.concat(Arrays.stream(common), Arrays.stream(daughter)).toArray(String[][]::new)
    );
}
