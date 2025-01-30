package com.rescuewheels.backend.service;

import com.rescuewheels.backend.dao.UserRepository;
import com.rescuewheels.backend.entity.User;
import com.rescuewheels.backend.enums.UserRoles;
import com.rescuewheels.backend.exception.ForbiddenOperationException;
import com.rescuewheels.backend.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User saveTechnician(User user) {
        user.setRoles(List.of(UserRoles.TECHNICIAN.getRole()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Optional<User> result = userRepository.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();

        if (result.isEmpty()){
            throw new UserNotFoundException("User id - " + id + " not found");
        }

        User userToBeDeleted = result.get();

        if (!userToBeDeleted.getId().equals(authenticatedUser.getId())
                && !authenticatedUser.getRoles().contains("ROLE_ADMIN")) {
            throw new ForbiddenOperationException("User id - " + id + " cannot be deleted by the authenticated user");
        }

        userRepository.delete(userToBeDeleted);
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
