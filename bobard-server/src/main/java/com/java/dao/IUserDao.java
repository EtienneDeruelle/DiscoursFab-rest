/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.dao;

import com.java.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Etienne
 */
public interface IUserDao extends CrudRepository<User, Long>{
    
    public Optional<User> findByUsername(String username);
    
    public Optional<User> findByToken(String token);    
}
