package com.ssafy.api.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.security.Key;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(MockitoExtension.class)
class JwtKeyTest {

    @Test
    void JwtKey_정상작동() {
        //given
        Pair<String, Key> givenPair = JwtKey.getRandomKey();
        String givenKid = givenPair.getFirst();

        //when
        Key actualKey = JwtKey.getKey(givenKid);

        //then
        then(actualKey).isEqualTo(givenPair.getSecond());
    }

    @Test
    void JwtKey_잘못된_Key를_가져올때() {
        //given
        String givenKid = "wrongKey";

        //when
        Key actualKey = JwtKey.getKey(givenKid);

        //then
        then(actualKey).isNull();
    }
}