package com.rescuewheels.backend.rest;

import com.rescuewheels.backend.entity.User;
import com.rescuewheels.backend.enums.UserRoles;
import com.rescuewheels.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/technician")
    public ResponseEntity<User> saveTechnician(@RequestBody User user) {
        user.setRoles(List.of(UserRoles.TECHNICIAN.getRole()));

        User savedUser = userService.save(user);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable("id") String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
