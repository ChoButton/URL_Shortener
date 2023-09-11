package com.chobutton.back.repository;

import com.chobutton.back.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.chobutton.back.enums.Role.ADMIN;
import static com.chobutton.back.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRoleRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Test
    @Transactional
    @DisplayName("1번 유저의 권한을 조회하면 USER, ADMIN 이 출력된다")
    public void findAllByUserIdTest(){
        //given
        int userId = 1;

        //when
        List<UserRole> roles = userRoleRepository.findAllByUserId(userId);

        //then
        assertThat(roles.get(0).getRole()).isEqualTo(USER);
        assertThat(roles.get(1).getRole()).isEqualTo(ADMIN);
    }

}
