package com.example.InventoryManagment.dtos;


import com.example.InventoryManagment.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "is required")
    private String name;

    @NotBlank(message = "is required")
    private String email;

    @NotBlank(message = "is required")
    private String password;

    private UserRole role;
}
