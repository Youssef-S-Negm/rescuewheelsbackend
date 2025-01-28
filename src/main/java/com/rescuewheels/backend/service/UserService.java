package com.rescuewheels.backend.service;

import com.rescuewheels.backend.dao.UserRepository;
import com.rescuewheels.backend.entity.User;
import com.rescuewheels.backend.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User deleteById(String id) {
        Optional<User> result = userRepository.findById(id);

        if (result.isEmpty()){
            throw new UserNotFoundException("User id - " + id + " not found");
        }

        User user = result.get();

        userRepository.delete(user);

        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
