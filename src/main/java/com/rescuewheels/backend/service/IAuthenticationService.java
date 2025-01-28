package com.rescuewheels.backend.service;

import com.rescuewheels.backend.dto.UserLoginDto;
import com.rescuewheels.backend.dto.UserRegistrationDto;
import com.rescuewheels.backend.entity.User;

public interface IAuthenticationService {

    User login(UserLoginDto input);

    User signUp(UserRegistrationDto input);
}
