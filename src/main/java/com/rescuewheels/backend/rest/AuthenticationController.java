package com.rescuewheels.backend.rest;

import com.rescuewheels.backend.dto.LoginResponse;
import com.rescuewheels.backend.dto.UserLoginDto;
import com.rescuewheels.backend.dto.UserRegistrationDto;
import com.rescuewheels.backend.entity.User;
import com.rescuewheels.backend.service.IAuthenticationService;
import com.rescuewheels.backend.service.IJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;
    private final IJwtService jwtService;

    @Autowired
    public AuthenticationController(IAuthenticationService authenticationService, IJwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginDto input) {
        User user = authenticationService.login(input);
        String token = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse(user, token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> signUp(@RequestBody UserRegistrationDto input) {
        User user = authenticationService.signUp(input);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
