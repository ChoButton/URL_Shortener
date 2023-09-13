package com.chobutton.back.service;

import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @Transactional
    @DisplayName("1번 유저를 조회할경우 email은 test1@test.com일것이다.")
    public void findByIdTest(){
        //given
        int id = 1;
        String email = "test1@test.com";

        //when
        UserDTO user = usersService.findById(id);

        //then
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @Transactional
    @DisplayName("email이 test1@test.com인 유저를 조회할경우 1번 유저일 것이다.")
    public void findByEmailTest(){
        //given
        int id = 1;
        String email = "test1@test.com";

        //when
        UserDTO user = usersService.findByEmail(email);

        //then
        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    @Transactional
    @DisplayName("1번 유저를 삭제할 경우 전체 유저의 수는 2명일 것이고 1번 유저의 정보는 없을 것이다.")
    public void deleteById(){
        //given
        int id = 1;

        // when
        usersService.deleteById(id);

        List<UserDTO> userDTOList = usersService.findAll();

        //then
        assertThat(userDTOList.size()).isEqualTo(2);
        assertThrows(NoSuchElementException.class, () -> usersService.findById(id));
    }

    @Test
    @Transactional
    @DisplayName("새로운 유저가 가입될 경우 전체 유저는 4일것이고," +
            "비밀번호는 암호화되어 저장된다.")
    public void saveTest(){
        //given
        String email = "test4@test.com";
        String password = "1234qwer";

        //when
        UserDTO user = UserDTO.builder()
                .email(email)
                .password(password)
                .build();
        usersService.save(user);

        List<UserDTO> userDTOList = usersService.findAll();

        //then
        assertThat(userDTOList.size()).isEqualTo(4);
        assertThat(bCryptPasswordEncoder.matches(password, userDTOList.get(3).getPassword())).isTrue();
    }

    @Test
    @Transactional
    @DisplayName("새로운 유저 4번을 생성후 입력한 다음 비밀번호 변경시 변경된 비밀번호가 적용될것이다.")
    public void updateTest(){
        //given
        String email = "test4@test.com";
        String password = "1234qwer";
        String newPassword = "qwer1234";

        //when
        UserDTO user = UserDTO.builder()
                .email(email)
                .password(password)
                .build();
        usersService.save(user);

        //새로운 유저 저장 직후 비밀번호 검증을 위한 유저 검색
        List<UserDTO> originPWUser = usersService.findAll();

        int id = originPWUser.get(3).getId();

        UserUpdateDTO updateUser = UserUpdateDTO.builder()
                .id(id)
                .email(email)
                .newPassword(newPassword)
                .originPassword(password)
                .build();
        usersService.update(updateUser);
        // update후 변경된 비밀번호 검증을 위한 유저 검색
        List<UserDTO> newPWUser = usersService.findAll();

        //then
        assertThat(bCryptPasswordEncoder.matches(password, originPWUser.get(3).getPassword())).isTrue();
        assertThat(bCryptPasswordEncoder.matches(newPassword, newPWUser.get(3).getPassword())).isTrue();
    }
}
