package com.example.InventoryManagment.services.impl;


import com.example.InventoryManagment.dtos.LoginRequest;
import com.example.InventoryManagment.dtos.RegisterRequest;
import com.example.InventoryManagment.dtos.Response;
import com.example.InventoryManagment.dtos.UserDTO;
import com.example.InventoryManagment.enums.UserRole;
import com.example.InventoryManagment.exception.InvalidCredentialsException;
import com.example.InventoryManagment.exception.NotFoundException;
import com.example.InventoryManagment.models.User;
import com.example.InventoryManagment.repository.UserRepository;
import com.example.InventoryManagment.security.JwtUtils;
import com.example.InventoryManagment.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;


    @Override
    public Response registerUser(RegisterRequest registerRequest) {
        //
        UserRole role = UserRole.MANAGER;
        if(registerRequest.getRole() != null){
            role = registerRequest.getRole();
        }

        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User was successfully registered")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new NotFoundException("Email not found"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
             throw new InvalidCredentialsException("Password Does Not match");
        }
        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User logged in successfully")
                .role(user.getRole())
                .token(token)
                .expirationTime("6 months")
                .build();
    }

    @Override
    public Response getAllUser() {
        // lấy danh sách người dung sắp xếp giảm dần theo "id"
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));

        // với mỗi user, xóa danh sách tránh trả dữ liệu không cần thiết
        users.forEach(user -> user.setTransactions(null));

        //Model mapper chuyển attribute user sang userDTO trả về những trường cần show cho khách hàng
        List<UserDTO> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .users(userDTOS)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {

        //lấy thông tin xác thực hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //lấy email
        String email = authentication.getName();

        // Tìm email
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

        user.setTransactions(null);

        // trả về người dùng hiện tại
        return user;
    }

    @Override
    public Response getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        userDTO.setTransactions(null);

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDTO)
                .build();
    }

    @Override
    public Response updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        if(userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if(userDTO.getPhoneNumber() != 0) existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        if(userDTO.getName() != null) existingUser.setName(userDTO.getName());
        if(userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .build();
    }

    @Override
    public Response getUserTransaction(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.getTransactions().forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("Success")
                .user(userDTO)
                .build();
    }
}
