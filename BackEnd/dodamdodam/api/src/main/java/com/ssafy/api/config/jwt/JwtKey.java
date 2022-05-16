package com.ssafy.api.config.jwt;

import com.ssafy.core.exception.CustomException;
import com.ssafy.core.exception.ErrorCode;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

public class JwtKey {

    private JwtKey() {
        throw new CustomException(ErrorCode.INTERVAL_SERVER_ERROR);
    }

    private static final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1", "SsafyTest1AndIwantToGet1prizeFromFinalProjectSoIHAVETOdoWorkVERYHARDefdkentl23kwmflobwWEKLNT3lwmEK",
            "key2", "e12fwgbsdgh34yhfszetzy45ehjkibn23klhjkeLKKWEN3kn4K#Ekwe92k3nEDknkrn3ktl34lhereksitnour24kdCOdme53kt",
            "key3", "EL3NGF0DJ4KSF8H983KLK5LSJDG90BU9XDFBK3KKKL34TMLJiohjrngknelkrgn92u34klnklgndklWghtkendKteiMdpet"
    );
    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private static final Random random = new Random();

    //랜덤한 KEY값 얻는 메소드
    public static Pair<String, Key> getRandomKey() {
        String kid = KID_SET[random.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    //kid로부터 Key값 가져오는 메소드
    public static Key getKey(String kid) {
        String key = SECRET_KEY_SET.getOrDefault(kid, null);
        if (key == null)
            return null;
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

}
