package com.example.InventoryManagment.controller;

import com.example.InventoryManagment.dtos.LoginRequest;
import com.example.InventoryManagment.dtos.RegisterRequest;
import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthorController {

   private final UserService userService;

   @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequest registerRequest){
       return ResponseEntity.ok(userService.registerUser(registerRequest));
   }

   @PostMapping("/login")
   public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest registerRequest){
       return ResponseEntity.ok(userService.loginUser(registerRequest));
   }
}
