package com.example.InventoryManagment.security;

import com.example.InventoryManagment.exception.NotFoundException;
import com.example.InventoryManagment.models.User;
import com.example.InventoryManagment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUserName(String username) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(username).orElse(() -> new NotFoundException("User Email Not found"));
    }
}
