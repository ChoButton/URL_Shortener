package com.chobutton.back.dto;
import com.chobutton.back.entity.Url;
import lombok.*;

@Getter @Setter @Builder @ToString
@AllArgsConstructor
@NoArgsConstructor
public class UrlDTO {

        private int id;

        private int userId;

        private String originUrl;

        private int requestCount;

}
