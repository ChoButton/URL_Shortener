package com.chobutton.back.service;

import com.chobutton.back.dto.UserRoleDTO;
import com.chobutton.back.enums.Role;
import com.chobutton.back.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.chobutton.back.enums.Role.ADMIN;
import static com.chobutton.back.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserRoleServiceTest {

    @Autowired
    UserRoleService userRoleService;

    @Test
    @Transactional
    @DisplayName("1번 유저의 권한을 조회할경우" +
            "ROLE_USER, ROLE_ADMIN을 리턴한다")
    public void findAllByUserIdTest(){
        //given
        int userId = 1;

        //when
        List<String> roles = userRoleService.getRolesName(userId);

        //then
        assertThat(roles.get(0)).isEqualTo("ROLE_USER");
        assertThat(roles.get(1)).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @Transactional
    @DisplayName("전체 권한을 조회할경우 8개가 조회된다")
    public void findAllTest(){
        //given

        //when
        List<UserRoleDTO> userRoleDTOS = userRoleService.findAll();

        //then
        assertThat(userRoleDTOS.size()).isEqualTo(8);
    }

    @Test
    @Transactional
    @DisplayName("1번 유저의 권한을 조회하면 2개가 조회될것이고" +
            "USER, ADMIN 권한이 출력된다")
    public void findByUserIdTest(){
        //given
        int userId = 1;

        //when
        List<UserRoleDTO> userRoleDTOS = userRoleService.findAllByUserId(userId);

        //then
        assertThat(userRoleDTOS.size()).isEqualTo(2);
        assertThat(userRoleDTOS.get(0).getRole()).isEqualTo(USER);
        assertThat(userRoleDTOS.get(1).getRole()).isEqualTo(ADMIN);
    }

    @Test
    @Transactional
    @DisplayName("1번권한을 조회할경우 userId는 1, role은 USER일것이다.")
    public void findByIdTest(){
        //given
        int Id = 1;

        //when
        UserRoleDTO userRoleDTO = userRoleService.findById(Id);

        //then
        assertThat(userRoleDTO.getUserId()).isEqualTo(1);
        assertThat(userRoleDTO.getRole()).isEqualTo(USER);
    }


    @Test
    @Transactional
    @DisplayName("새로운 유저가 생성되어 권한이 주어질 경우 전체 권한이 9개일것이며," +
            "마지막 인덱스와 일치할 것이다.")
    public void saveTest(){
        //given
        int userId = 4;
        Role role = USER;

        //when
        UserRoleDTO roles = UserRoleDTO.builder()
                .userId(userId)
                .role(role)
                .build();

        userRoleService.save(roles);
        List<UserRoleDTO> userRoleDTOS = userRoleService.findAll();
        //then
        assertThat(userRoleDTOS.size()).isEqualTo(9);
        assertThat(userRoleDTOS.get(userRoleDTOS.size()-1).getUserId()).isEqualTo(userId);
        assertThat(userRoleDTOS.get(userRoleDTOS.size()-1).getRole()).isEqualTo(role);
    }

    @Test
    @Transactional
    @DisplayName("전체 권한 리스트중 1번 권한을 삭제하면 총 권한의 갯수는 7개" +
            "1번 유저의 권한은 1개이다.")
    public void deleteByIdTest(){
        //given
        int id = 1;
        int userId = 1;

        //when
        userRoleService.deleteById(id);

        List<UserRoleDTO> userRoleDTOS = userRoleService.findAll();
        List<UserRoleDTO> userId1DTO = userRoleService.findAllByUserId(userId);

        //then
        assertThat(userRoleDTOS.size()).isEqualTo(7);
        assertThat(userId1DTO.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DisplayName("1번 유저의 권한을 모두 삭제할 경우 1번 유저의 권한 데이터는 없을 겂이고" +
            "전체 권한 갯수는 6개일 것이다.")
    public void deleteAllByUserIdTest(){
        //given
        int userId = 1;

        //when
        userRoleService.deleteAllByUserId(userId);
        List<UserRoleDTO> userRoleDTOS = userRoleService.findAll();
        ;

        //then
        assertThat(userRoleDTOS.size()).isEqualTo(6);
        assertThrows(BadRequestException.class, () -> userRoleService.findAllByUserId(userId));
    }
}
