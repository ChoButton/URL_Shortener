package com.chobutton.back.service;

import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {

    UserDTO findById(int id);

    UserDTO findByEmail(String email);

    List<UserDTO> findAll();

    void deleteById(int id);

    ResponseEntity<String> save(UserDTO signupUser);

    ResponseEntity<String> update(UserUpdateDTO userUpdate);

    // 로그인시 결과와 token 정보를 보낼수 있는 기능
    ResponseEntity<String> loginAuthentication(UserDTO longinUser);
}
