package com.example.InventoryManagment.models;

import com.example.InventoryManagment.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Is required")
    private String name;

    @Column(unique = true)
    @NotBlank(message = "Is required")
    private String email;

    @NotBlank(message = "Is required")
    private String password;

    @Column(name = "phone_number")
    @NotBlank(message = "Is required")
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private final LocalDateTime createAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", transaction=" + transactions +
                ", role=" + role +
                ", createAt=" + createAt +
                '}';
    }
}
