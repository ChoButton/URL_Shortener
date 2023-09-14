package com.chobutton.back.config.jwt;

import com.chobutton.back.entity.User;
import com.chobutton.back.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    @Transactional
    @DisplayName("1번 유저 정보와 토큰 만료기간을 전달하여 토큰이 정상적으로 생성되며," +
            "생성된 토큰의 id값과 1번 유저의 id가 일치할것이다.")
    public void generateTokenTest(){
        //given
        int userId = 1;
        User user = userRepository.findById(userId).orElse(null);

        //when
        String token = tokenProvider.generateToken(user, Duration.ofDays(14));

        int tokenInUserId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Integer.class);

        //then
        assertThat(userId).isEqualTo(tokenInUserId);
    }

    @Test
    @Transactional
    @DisplayName("만료된 토큰의 경우 검증에 실패한다")
    public void validTokenFalseTest(){
        //given
        String token = tokenProvider
                .generateToken(
                        userRepository.findById(1).orElse(null),
                        Duration.ofDays(-7)
                );

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @Transactional
    @DisplayName("유효한 토큰의 경우 검증에 성공한다")
    public void validTokenTrueTest(){
        //given
        String token = tokenProvider
                .generateToken(
                        userRepository.findById(1).orElse(null),
                        Duration.ofDays(7)
                );

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("1번유저로 토큰이 생성될경우" +
            "권한인 USER, ADMIN으로 인증정보를 가져올수있다.")
    public void getAuthenticationTest(){
        //given
        String token = tokenProvider
                .generateToken(
                        userRepository.findById(1).orElse(null),
                        Duration.ofDays(7)
                );
        //when
        Authentication authentication = tokenProvider.getAuthentication(token);
        List<String> userRoles = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        //then
        assertThat(userRoles.get(0)).isEqualTo("ROLE_USER");
        assertThat(userRoles.get(1)).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @Transactional
    @DisplayName("1번 유저를 통해 생성된 토큰에서 userId를 가져올수 있다.")
    public void getUserIdTest(){
        //given
        int userId = 1;
        String token = tokenProvider
                .generateToken(
                        userRepository.findById(1).orElse(null),
                        Duration.ofDays(7)
                );
        //when
        int userIdByToken = tokenProvider.getUserId(token);

        //then
        assertThat(userId).isEqualTo(userIdByToken);
    }
}
