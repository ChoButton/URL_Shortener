package com.chobutton.back.service;

import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {

    UserDTO findById(int id);

    UserDTO findByEmail(String email);

    List<UserDTO> findAll();

    void deleteById(int id);

    void save(UserDTO userDTO);

    void update(UserUpdateDTO userUpdateDTO);
}
