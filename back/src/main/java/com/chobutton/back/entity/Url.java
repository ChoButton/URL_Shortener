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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 2000)
    private String originUrl;

    @ColumnDefault("0")
    private int requestCount;


    // save 기능을 위한 인스턴스 멤버변수인 User를 입력하기 위한 생성자
    public Url(User user, String originUrl) {
        this.user = user;
        this.originUrl = originUrl;
    }
}
