package com.chobutton.back.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
/*
 * 사용자 암호 변경시 검증 로직을 위한
 * 원래 password, 변경 password 정보를 담고있는
 * 객체용 DTO
 */
public class UserUpdateDTO {
    private int id;

    private String email;

    private String originPassword;

    private String newPassword;
}
