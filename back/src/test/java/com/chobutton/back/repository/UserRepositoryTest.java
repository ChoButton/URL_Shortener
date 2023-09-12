package com.chobutton.back.repository;

import com.chobutton.back.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("test1@test.com으로 유저를 정보를 조회할경우 userId 1번이 조회된다")
    public void findByEmailTest(){
        //given
        String email = "test1@test.com";

        //when
        User user = userRepository.findByEmail(email);

        //then
        assertThat(user.getId()).isEqualTo(1);
    }
}
