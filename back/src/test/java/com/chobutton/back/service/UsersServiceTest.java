package com.chobutton.back.service;

import com.chobutton.back.config.jwt.TokenProvider;
import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserRoleDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import com.chobutton.back.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.chobutton.back.enums.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    UrlService urlService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    TokenProvider tokenProvider;

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
    @DisplayName("1번 유저를 삭제할 경우 전체 유저의 수는 2명일 것이고 1번 유저의 정보는 없을 것이며," +
            "1번 유저가 등록한 URl및 1번 유저의 권한도 없을것이다.")
    public void deleteById(){
        //given
        int id = 1;

        // when
        usersService.deleteById(id);
        List<UserDTO> userDTOList = usersService.findAll();

        //then
        assertThat(userDTOList.size()).isEqualTo(2);
        assertThrows(BadRequestException.class, () -> usersService.findById(id));
        assertThrows(BadRequestException.class, () -> userRoleService.findAllByUserId(id));
        assertThrows(BadRequestException.class, () -> urlService.findAllByUserId(id));
    }

    @Test
    @Transactional
    @DisplayName("새로운 유저가 가입될 경우 전체 유저는 4일것이고," +
            "비밀번호는 암호화되어 저장된다." +
            "이미 가입된 이메일로 가입할경우 400에러가 리턴된다" +
            "또한 새로 해당 유저의 USER권한이 생성된다.")
    public void saveTest(){
        //given
        String email = "test4@test.com";
        String password = "1234qwer";
        String user1Email = "test1@test.com";

        //when
        UserDTO user = UserDTO.builder()
                .email(email)
                .password(password)
                .build();
        ResponseEntity<String> signupUser = usersService.save(user);
        List<UserRoleDTO> userRoleDTOList = userRoleService
                .findAllByUserId(usersService.findByEmail(email).getId());
        user.setEmail(user1Email);
        List<UserDTO> userDTOList = usersService.findAll();


        //then
        assertThat(userDTOList.size()).isEqualTo(4);
        assertThat(bCryptPasswordEncoder.matches(password, userDTOList.get(3).getPassword())).isTrue();
        assertThat(HttpStatus.OK).isEqualTo(signupUser.getStatusCode());
        assertThat("회원가입이 정상적으로 완료되었습니다.").isEqualTo(signupUser.getBody());
        assertThrows(BadRequestException.class, () -> usersService.save(user));
        assertThat(userRoleDTOList.get(0).getRole()).isEqualTo(USER);
    }

    @Test
    @Transactional
    @DisplayName("새로운 유저 4번을 생성후 입력한 다음 비밀번호 변경시 변경된 비밀번호가 적용될것이며," +
            "잘못된 변경전 비밀번호를 입력하면 400에러를 리턴할것이다.")
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
        ResponseEntity<String> responseOk = usersService.update(updateUser);
        updateUser.setOriginPassword("123123123123123");
        // update후 변경된 비밀번호 검증을 위한 유저 검색
        List<UserDTO> newPWUser = usersService.findAll();

        //then
        assertThat(bCryptPasswordEncoder.matches(password, originPWUser.get(3).getPassword())).isTrue();
        assertThat(bCryptPasswordEncoder.matches(newPassword, newPWUser.get(3).getPassword())).isTrue();
        assertThat(HttpStatus.OK).isEqualTo(responseOk.getStatusCode());
        assertThat("비밀번호가 정상적으로 변경되었습니다.")
                .isEqualTo(responseOk.getBody());
        assertThrows(BadRequestException.class, () -> usersService.update(updateUser));
    }

    @Test
    @Transactional
    @DisplayName("새로 가입한 유저가 로그인했을경우 틀린 정보를 입력하면 400에러가 리턴된다")
    public void loginAuthenticationTest(){
        //given
        String email = "test4@test.com";
        String errorEmail = "error@error.com";
        String password = "1234qwer";
        String errorPw = "error";
        UserDTO userOk = UserDTO.builder()
                        .email(email)
                                .password(password)
                                        .build();
        UserDTO userEmailError = UserDTO.builder()
                        .email(errorEmail)
                                .password(password)
                                        .build();
        UserDTO userPwError = UserDTO.builder()
                        .email(email)
                                .password(errorPw)
                                        .build();
        usersService.save(userOk);
        int userId = usersService.findByEmail(userOk.getEmail()).getId();
        //when
        ResponseEntity<?> responseOk = usersService.loginAuthentication(userOk);


        //then
        assertThat(HttpStatus.OK).isEqualTo(responseOk.getStatusCode());
        assertThat(userId).isEqualTo(tokenProvider.getUserId((String)responseOk.getBody()));
        assertThrows(BadRequestException.class, () -> usersService.loginAuthentication(userEmailError));
        assertThrows(BadRequestException.class, () -> usersService.loginAuthentication(userPwError));
    }
}
