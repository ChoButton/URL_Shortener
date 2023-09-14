package com.chobutton.back.controller;

import com.chobutton.back.config.jwt.TokenProvider;
import com.chobutton.back.dto.UserDTO;
import com.chobutton.back.dto.UserUpdateDTO;
import com.chobutton.back.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TokenProvider tokenProvider;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> loginAuthentication(@RequestBody UserDTO loginUser){
        return usersService.loginAuthentication(loginUser);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<String> signup (@RequestBody UserDTO signupUser){
        return usersService.save(signupUser);
    }

    @RequestMapping(value = "/update/password", method = RequestMethod.PATCH)
    public ResponseEntity<String> updatePassword (@RequestBody UserUpdateDTO updateUser){
        return usersService.update(updateUser);
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser (@PathVariable int userId){
        usersService.deleteById(userId);
        return ResponseEntity.ok().body("정상적으로 유저가 삭제되었습니다.");
    }
}
