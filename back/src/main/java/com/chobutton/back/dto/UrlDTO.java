package com.chobutton.back.dto;

import com.chobutton.back.entity.Url;
import com.chobutton.back.entity.User;
import lombok.*;

public class UrlDTO {

    // 일반적인 조회기능을 수행하기 위한 DTO
    @Getter @Setter @Builder @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Find {
        private int id;

        private int userId;

        private String originUrl;

        private int requestCount;

        // 엔터티를 DTO로 변환하여 불러오는 메서드 정의
        public static UrlDTO.Find fromUrlEntity(Url url){
            return UrlDTO.Find.builder()
                    .id(url.getId())
                    .userId(url.getUser().getId())
                    .originUrl(url.getOriginUrl())
                    .requestCount(url.getRequestCount())
                    .build();
        }
    }

    // 리다이렉트를 위한 조회의 경우에 불필요한 데이터의 전송을 막기위한 DTO
    @Getter @Setter @Builder @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FindForEncoding {
        private int id;
    }

    // 엔터티를 DTO로 변환하여 불러오는 메서드 정의
    public static UrlDTO.FindForEncoding fromUrlEntity(Url url){
        return UrlDTO.FindForEncoding.builder()
                .id(url.getId())
                .build();
    }

    @Getter @Setter @Builder @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Save {
        private User user;

        private String originUrl;

        // UrlResponseDTO.Save 객체를 Entity로 변환해주는 메서드
        public static Url toUrlEntity(UrlDTO.Save urlDTOSave){
            return new Url(urlDTOSave.getUser(), urlDTOSave.getOriginUrl());
        }

    }

}
