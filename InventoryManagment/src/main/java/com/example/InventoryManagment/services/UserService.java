package com.example.InventoryManagment.services;


import com.example.InventoryManagment.dtos.LoginRequest;
import com.example.InventoryManagment.dtos.RegisterRequest;
import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.UserDTO;
import com.example.InventoryManagment.models.User;
import org.springframework.stereotype.Repository;

public interface UserService {

    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUser();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDTO userDTO);

    Response deleteUser(Long id);

    Response getUserTransaction(Long id);
}
