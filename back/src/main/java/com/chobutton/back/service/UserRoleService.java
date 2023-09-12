package com.chobutton.back.service;

import com.chobutton.back.dto.UserRoleDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRoleService {

    // role의 roleName을 String 형태로 반환
    List<String> getRolesName(int userId);

    void save(UserRoleDTO userRoleDTO);

    List<UserRoleDTO> findAll();

    List<UserRoleDTO> findAllByUserId(int userId);

    UserRoleDTO findById(int id);

    void deleteById(int id);

    void deleteAllByUserId(int userId);
}
