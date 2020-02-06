/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.security;

import com.java.entity.User;
import com.java.security.IUserAuthentificationService;
import com.java.service.IUserService;
import java.util.Optional;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 *
 * @author Etienne
 */
@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserAuthentificationServiceImpl implements IUserAuthentificationService{

    @NonNull
    IUserService userService;
    
    @Override
    public Optional<String> login(String username, String password) {
        return userService.login(username, password);
    }

    @Override
    public Optional<User> findByToken(String token) {
        return userService.findByToken(token);
    }

    @Override
    public void logout(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
