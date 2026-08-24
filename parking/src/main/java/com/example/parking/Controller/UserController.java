package com.example.parking.Controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.parking.DTO.UserResponse;
import com.example.parking.Service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{username}/admin")
    public UserResponse makeAdmin(@PathVariable String username) {

        return userService.makeAdmin(username);
    }
    @GetMapping
    public Page<UserResponse> getAllUsers(Pageable pageable){
        return userService.getAllUsers(pageable);
    }
    @GetMapping("/search")
    public Page<UserResponse> searchUsers(@RequestParam String username,Pageable pageable) {

        return userService.searchUsers(username, pageable);
    }
}
