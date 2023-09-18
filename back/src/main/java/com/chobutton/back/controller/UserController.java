package com.chobutton.back.controller;

import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import com.chobutton.back.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> loginAuthentication(@RequestBody UserDTO loginUser){
        return usersService.loginAuthentication(loginUser);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signup (@RequestBody UserDTO signupUser){
        System.out.println("DTO 확인 : " + signupUser.toString());
            return usersService.save(signupUser);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/update/password", method = RequestMethod.PATCH)
    public ResponseEntity<String> updatePassword (@RequestBody UserUpdateDTO updateUser){
        return usersService.update(updateUser);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser (@PathVariable int userId){
        usersService.deleteById(userId);
        return ResponseEntity.ok().body("탈퇴가 정상적으로 처리되었습니다.");
    }
}
