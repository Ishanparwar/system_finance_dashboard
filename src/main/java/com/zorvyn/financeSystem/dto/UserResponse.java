package com.zorvyn.financeSystem.dto;

import com.zorvyn.financeSystem.model.enums.Role;
import com.zorvyn.financeSystem.model.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Status status;
}
