package com.chobutton.back.repository;

import com.chobutton.back.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    List<UserRole> findAllByUserId(int userId);

    void deleteAllByUserId(int userId);
}
