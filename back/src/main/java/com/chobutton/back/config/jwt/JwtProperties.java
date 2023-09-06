package com.chobutton.back.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String issuer; //jwt.issuer 값을 저장
    private String secretKey; // jwt.secret_Key값을 저장
}
