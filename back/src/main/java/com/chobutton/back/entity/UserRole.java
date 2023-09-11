package com.chobutton.back.entity;

import com.chobutton.back.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Enumerated(EnumType.STRING) // ENUM값을 문자로 저장해주는 어노테이션
    @Column(length = 10)
    private Role role;

}
