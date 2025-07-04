package com.curootest.clientback.web.controller;

import com.curootest.clientback.domain.UserDTO;
import com.curootest.clientback.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.register(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password)
                .map(jwt -> new ResponseEntity<>(jwt, HttpStatus.OK))
                .orElse(new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> update(@PathVariable("userId") int userId, @RequestBody UserDTO userDTO) {
        return userService.update(userId, userDTO)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
