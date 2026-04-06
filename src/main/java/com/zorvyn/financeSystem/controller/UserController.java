package com.zorvyn.financeSystem.controller;

import com.zorvyn.financeSystem.dto.CreateUserRequest;
import com.zorvyn.financeSystem.dto.UserResponse;
import com.zorvyn.financeSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse createUser(
            @RequestBody CreateUserRequest request,
            @RequestParam(required = false) Long userId) {

        return userService.createUser(request, userId);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PatchMapping("/{id}/status")
    public void changeStatus (@PathVariable Long id, @RequestParam String status,
                              @RequestParam Long currentUserId){
        userService.changeUserStatus(id ,status, currentUserId);
    }

}
