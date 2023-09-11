package com.chobutton.back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false, length = 2000)
    private String originUrl;

    @ColumnDefault("0")
    private int requestCount;


    // 접속횟수 증가를 위한 메서드 추가
    public void incrementRequestCount() {
        this.requestCount += 1;
    }
}
