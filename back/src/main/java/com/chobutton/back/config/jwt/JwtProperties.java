package com.chobutton.back.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
// application.yml에서 전달된 issuer secretKey 를 멤버변수로 저장하는 클래스
public class JwtProperties {
    private String issuer; //jwt.issuer 값을 저장
    private String secretKey; // jwt.secret_Key값을 저장
}
