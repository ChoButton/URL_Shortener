package com.chobutton.back.dto;
import lombok.*;

@Getter @Setter @Builder @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UrlDTO {

        private int id;

        private int userId;

        private String originUrl;

        private int requestCount;

        // 등록된 Url 목록 조회시 단축된 Url주소를 바로 볼수 있도록 멤버변수 추가
        private String shortenUrl;
}
