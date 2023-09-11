package com.chobutton.back.config.jwt;

import com.chobutton.back.entity.User;
import com.chobutton.back.service.UsersService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private final UsersService usersService;

    private String makeToken(Date expiry, User user){
        // 현재시간 기준으로 토큰 발급 날짜 저장
        Date now = new Date();
        // 빌더패턴 으로 토큰 생성
        return Jwts.builder()
                // 헤더 타입은 JWT
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                // application.yml 에 기입된 내용 전달
                .setIssuer(jwtProperties.getIssuer())
                // 발급시간 = 현재시간
                .setIssuedAt(now)
                // 만료시간
                .setExpiration(expiry)
                // 해당 user정보중 유일한 값을 설정
                .setSubject(user.getEmail())
                // user의 PK제공
                .claim("id", user.getId())
                // 암호화 알고리즘과 비밀키값
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                // .build가 아닌 .compact
                .compact();
    }

    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // 토큰 유효성 검사
    public boolean validToken(String token){
        try{
            // 토큰 파싱
            Jwts.parser()
                    // 복호화를 위해 암호화때 사용한 비밀키 입력
                    .setSigningKey(jwtProperties.getSecretKey())
                    // 검증 타겟이 되는 토큰 입력
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                // 복호화를 위한 비밀키 제공
                .setSigningKey(jwtProperties.getSecretKey())
                // 토큰을 제공
                .parseClaimsJws(token)
                // 유저 정보를 클레임 형태로 리턴
                .getBody();
    }

    // 토큰을 통해 인증정보를 읽어오는 기능
    public Authentication getAuthentication(String token){
        // 위에 정의된 메서드를 통해 유저 정보 저장
        Claims claims = getClaims(token);


    }
}
