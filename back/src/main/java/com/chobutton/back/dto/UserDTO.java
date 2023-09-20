package com.chobutton.back.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;

    private String email;

    private String password;

    private String originUrl;
}
