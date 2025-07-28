package com.example.InventoryManagment.controller;


import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.UserDTO;
import com.example.InventoryManagment.models.User;
import com.example.InventoryManagment.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/getTransaction/{id}")
    public ResponseEntity<Response> getTransaction(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserTransaction(id));
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentLoggedInUser());
    }
}
