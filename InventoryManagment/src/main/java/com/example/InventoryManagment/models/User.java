package com.example.InventoryManagment.models;

import com.example.InventoryManagment.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Is required")
    private String name;

    @NotBlank(message = "Is required")
    private String email;

    @NotBlank(message = "Is required")
    private String password;

    @NotBlank(message = "Is required")
    private int phoneNumber;

    @OneToMany(mappedBy = "users")
    private List<Transaction> transaction;

    @Enumerated(EnumType.STRING)
    private UserRole user;

    private final LocalDateTime createAt = LocalDateTime.now();
}
