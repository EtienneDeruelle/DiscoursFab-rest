/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.service;

import com.java.entity.User;
import java.util.Optional;

/**
 *
 * @author Etienne
 */
public interface IUserService {

    User save(User user);

    Optional<User> find(String id);

    Optional<User> findByUsername(String username);
    
    Optional<String> login(String username, String password);
    
    Optional<User> findByToken(String token);
}
