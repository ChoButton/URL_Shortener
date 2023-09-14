package com.chobutton.back.controller;

import com.chobutton.back.config.jwt.TokenProvider;
import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import com.chobutton.back.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersService usersService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    @DisplayName("새로 가입한 유저가 로그인했을경우 틀린 이메일을 입력하면" +
            "400에러와 가입이 안된 사용자 입니다. 메세지 리턴" +
            "비밀번호가 틀릴경우 400에러와 비밀번호가 틀렸습니다. 리턴된다.")
    public void loginAuthenticationTest() throws Exception{
        //given
        String url = "/login";
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

        // 각 객체를 Json으로 역직렬화
        final String requestOk = objectMapper.writeValueAsString(userOk);
        final String requestEmailError = objectMapper.writeValueAsString(userEmailError);
        final String requestPwError = objectMapper.writeValueAsString(userPwError);

        //when
        ResultActions resultOk = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestOk))
                .andExpect(status().isOk());
        ResultActions resultEmailError = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestEmailError))
                .andExpect(status().isBadRequest());
        ResultActions resultPwError = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPwError))
                .andExpect(status().isBadRequest());


        int userId = usersService.findByEmail(userOk.getEmail()).getId();

        // Body에 있는 토큰 및 메세지를 얻어내기 위한 코드
        int tokenUserId = tokenProvider.getUserId(resultOk.andReturn().getResponse().getContentAsString());
        String emailError = resultEmailError.andReturn().getResponse().getContentAsString();
        String pwError = resultPwError.andReturn().getResponse().getContentAsString();

        //then
        assertThat(userId).isEqualTo(tokenUserId);
        assertThat("가입이 안된 사용자 입니다.").isEqualTo(emailError);
        assertThat("비밀번호가 틀렸습니다.").isEqualTo(pwError);
    }

    @Test
    @Transactional
    @DisplayName("정상적으로 가입될경우 200 상태코드와 회원가입 성공 메세지가 리턴되며," +
            "이미 가입된 이메일로 가입할경우 400에러와 메세지가 리턴된다")
    public void saveTest() throws Exception{
        //given
        String url = "/signup";
        String email = "test4@test.com";
        String password = "1234qwer";
        String user1Email = "test1@test.com";

        //when
        UserDTO user = UserDTO.builder()
                .email(email)
                .password(password)
                .build();

        final String signupUser = objectMapper.writeValueAsString(user);
        user.setEmail(user1Email);
        final String signupFail = objectMapper.writeValueAsString(user);

        ResultActions resultOk = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupUser))
                .andExpect(status().isOk());
        ResultActions resultFail = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupFail))
                .andExpect(status().isBadRequest());


        //then
        assertThat("회원가입이 정상적으로 완료되었습니다.")
                .isEqualTo(resultOk.andReturn().getResponse().getContentAsString());
        assertThat("이미 가입된 사용자 입니다.")
                .isEqualTo(resultFail.andReturn().getResponse().getContentAsString());
    }

    @Test
    @Transactional
    @DisplayName("새로운 유저 4번을 생성후 입력한 다음 비밀번호 변경시 변경된 비밀번호가 적용될것이며," +
            "잘못된 변경전 비밀번호를 입력하면 400에러와 메세지를 리턴할것이다.")
    public void updatePasswordTest() throws Exception{
        //given
        String email = "test4@test.com";
        String password = "1234qwer";
        String newPassword = "qwer1234";
        String signupUrl = "/signup";
        String updateUrl = "/update/password";

        //when
        UserDTO user = UserDTO.builder()
                .email(email)
                .password(password)
                .build();
        final String save = objectMapper.writeValueAsString(user);
        mockMvc.perform(post(signupUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(save))
                .andExpect(status().isOk());

        UserUpdateDTO updateUser = UserUpdateDTO.builder()
                .id(usersService.findByEmail(email).getId())
                .originPassword(password)
                .newPassword(newPassword)
                .build();
        final String updateOk = objectMapper.writeValueAsString(updateUser);
        updateUser.setOriginPassword("failTest");
        final String updateFail = objectMapper.writeValueAsString(updateUser);

        // then
        ResultActions resultOk = mockMvc.perform(patch(updateUrl)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(updateOk))
                .andExpect(status().isOk());
        ResultActions resultFail = mockMvc.perform(patch(updateUrl)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(updateFail))
                .andExpect(status().isBadRequest());
        assertThat("비밀번호가 정상적으로 변경되었습니다.")
                .isEqualTo(resultOk.andReturn().getResponse().getContentAsString());
        assertThat("입력하신 기존 비밀번호가 다릅니다.")
                .isEqualTo(resultFail.andReturn().getResponse().getContentAsString());
    }


}
