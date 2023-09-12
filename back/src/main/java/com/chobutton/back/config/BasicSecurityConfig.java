package com.chobutton.back.config;

import com.chobutton.back.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration // 설정 클래스 상위에 붙이는 어노테이션
@RequiredArgsConstructor
// 베이직 방식 인증을 사용하도록 설정하는 파일
public class BasicSecurityConfig {
    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
}
