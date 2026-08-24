package com.example.parking.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.parking.Exceptions.DoesNotExistException;
import com.example.parking.Repository.UserRepository;
import com.example.parking.UserType;
import com.example.parking.DTO.UserResponse;
import com.example.parking.model.Users;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse makeAdmin(String username) {

        Users user = userRepository.findByUsername(username).orElseThrow(() -> new DoesNotExistException("User not found"));

        user.setRole(UserType.ADMIN);

        userRepository.save(user);
        return new UserResponse(user.getUsername(), user.getRole());
    }
    public Page<UserResponse> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(user -> new UserResponse(user.getUsername(),user.getRole()));
    }
    public Page<UserResponse> searchUsers(
        String username,
        Pageable pageable) {

    return userRepository.findByUsernameContainingIgnoreCase(username, pageable).map(user -> new UserResponse(user.getUsername(),user.getRole() ));
}
}