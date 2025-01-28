package com.rescuewheels.backend.service;

import com.rescuewheels.backend.dao.UserRepository;
import com.rescuewheels.backend.dto.UserLoginDto;
import com.rescuewheels.backend.dto.UserRegistrationDto;
import com.rescuewheels.backend.entity.User;
import com.rescuewheels.backend.enums.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthenticationService implements IAuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public User login(UserLoginDto input) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
        ));

        return userRepository.findByEmail(input.getEmail());
    }

    @Override
    @Transactional
    public User signUp(UserRegistrationDto input) {
        User user = new User(
                input.getFirstName(),
                input.getLastName(),
                input.getEmail(),
                passwordEncoder.encode(input.getPassword()),
                input.getPhoneNumber(),
                List.of(UserRoles.USER.getRole())
        );

        return userRepository.save(user);
    }
}
