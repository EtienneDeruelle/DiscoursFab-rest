/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.controller;

import com.java.entity.User;
import com.java.service.IUserService;
import lombok.AccessLevel;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java.security.IUserAuthentificationService;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *
 * @author Etienne
 */
@RestController
@RequestMapping("/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class UserPrivateController {
    
    @NonNull
    IUserAuthentificationService authentification;
    
    @GetMapping("/logout")
    boolean logout(@AuthenticationPrincipal final User user){
        authentification.logout(user);
        return true;
    }
    
    @GetMapping("/current")
    User current(@RequestHeader(value="AUTHORIZATION") final String token){
        return authentification.findByToken(token).orElseThrow(() -> new SecurityException("Erreur lors de la récupération du token."));
    }
    
}
