/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.controller;

import com.java.entity.User;
import com.java.service.IUserService;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.java.security.IUserAuthentificationService;

/**
 *
 * @author Etienne
 */
@RestController
@RequestMapping("/public/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class UserPublicController {

    @NonNull
    IUserAuthentificationService authentificationService;
    @NonNull
    IUserService userService;

    @PostMapping("/register")
    String register(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        userService.save(User
                .builder()
                .username(username)
                .password(password)
                .build()
        );
        return login(username, password);
    }

    @PostMapping("/login")
    String login(@RequestParam("username") final String username, @RequestParam("password") final String password) {
        return authentificationService
                .login(username, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    }

}
