/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.service.impl;

import com.java.dao.IUserDao;
import com.java.entity.User;
import com.java.service.IUserService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Etienne
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserDao userDao;

    @Override
    public User save(final User user) {
        return userDao.save(user);
    }

    @Override
    public Optional<User> find(String id) {
        return userDao.findById(Long.valueOf(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Optional<String> login(String username, String password) {
        final User user = userDao.findByUsername(username)
                .orElseThrow(SecurityException::new);
        //vérifier le mdp
        final String uuid = UUID.randomUUID().toString();
        user.setToken(uuid);
        userDao.save(user);
        return Optional.of(uuid);
    }

    @Override
    public Optional<User> findByToken(final String token) {
        return userDao.findByToken(token);
    }

}
