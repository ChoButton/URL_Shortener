package com.chobutton.back.dto;

import com.chobutton.back.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO {

    private int id;

    private int userId;

    private Role role;

}
