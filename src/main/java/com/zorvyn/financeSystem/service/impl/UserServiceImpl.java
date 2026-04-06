package com.zorvyn.financeSystem.service.impl;

import com.zorvyn.financeSystem.dto.CreateUserRequest;
import com.zorvyn.financeSystem.dto.UserResponse;
import com.zorvyn.financeSystem.exception.BadRequestException;
import com.zorvyn.financeSystem.exception.ResourceNotFoundException;
import com.zorvyn.financeSystem.exception.UnauthorizedException;
import com.zorvyn.financeSystem.model.User;
import com.zorvyn.financeSystem.model.enums.Role;
import com.zorvyn.financeSystem.model.enums.Status;
import com.zorvyn.financeSystem.repository.UserRepository;
import com.zorvyn.financeSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request, Long currentUserId) {

        long userCount = userRepository.count(); //To check if the first user is present

        if(userCount == 0) {
            if(request.getRole() != Role.ADMIN) {
                throw new BadRequestException("First user must be ADMIN"); //As only admin can create rest.
            }
            if(userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new BadRequestException("Email already exists");
            }
            return saveUser(request);
        }

        if(currentUserId == null) {
            throw new UnauthorizedException("Admin userId is required");
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admin can create users");
        }
        if(currentUser.getStatus() != Status.ACTIVE) {
            throw new UnauthorizedException("Inactive admin cannot perform this action");
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        return saveUser(request);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public void changeUserStatus(Long id, String status, Long currentUserId) {

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid AdminId "));

        if(currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admin can change user status");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        try {
            user.setStatus(Status.valueOf(status));
        }catch (Exception e){
            throw new BadRequestException("Status can only be 'ACTIVE' or INACTIVE");
        }
        userRepository.save(user);
    }

    private UserResponse saveUser(CreateUserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(request.getRole())
                .status(Status.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
