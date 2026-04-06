package com.zorvyn.financeSystem.service;

import com.zorvyn.financeSystem.dto.CreateUserRequest;
import com.zorvyn.financeSystem.dto.UserResponse;

public interface UserService {
//    UserResponse createUser(CreateUserRequest request);

    UserResponse createUser(CreateUserRequest request, Long userId);

    UserResponse getUserById(Long id);

    void changeUserStatus(Long id, String status, Long currentUserId);
}
