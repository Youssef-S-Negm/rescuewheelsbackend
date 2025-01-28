package com.rescuewheels.backend.service;

import com.rescuewheels.backend.entity.User;

import java.util.List;

public interface IUserService {

    User save(User user);

    User deleteById(String id);

    User findByEmail(String email);

    List<User> findAll();
}
