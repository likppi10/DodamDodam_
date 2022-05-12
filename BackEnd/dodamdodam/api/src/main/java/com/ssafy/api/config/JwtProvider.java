package com.ssafy.api.config;

import com.ssafy.core.entity.User;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    final int JWT_TOKEN_EXPIRATION_TIME =  30 * 60 * 1000 * 600;
    final int REFRESH_TOKEN_EXPIRATION_TIME = 15 * 24 * 60 * 60 * 1000;

    private final UserDetailsService userDetailsService;

    // Jwt 토큰 생성
    public String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Pair<String, Key> key = JwtKey.getRandomKey();
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + JWT_TOKEN_EXPIRATION_TIME)) // set Expire Time
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // kid
                .signWith(key.getSecond()) // signature
                .compact();
    }

    // 로그인시 RefreshToken 발급
    public String createRefreshToken() {
        Pair<String, Key> key = JwtKey.getRandomKey();
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME)) // set Expire Time
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // kid
                .signWith(key.getSecond()) // signature
                .compact();
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public Long getUserPk(String token) {
        String userPK = Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.getInstance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.parseLong(userPK);
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(this.getUserPk(token)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKeyResolver(SigningKeyResolver.getInstance)
                    .build().parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //만료됐지만 유효한 jwt에서 userPk 빼오기.
    public Long getUserPkFromExpiredToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKeyResolver(SigningKeyResolver.getInstance)
                    .build().parseClaimsJws(jwtToken);
            if (claims.getBody().getExpiration().before(new Date())) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
        } catch (ExpiredJwtException e) {
            return Long.parseLong(e.getClaims().getSubject());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return null;
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

}
