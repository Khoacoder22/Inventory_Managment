package com.example.InventoryManagment.security;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomerDetailsService customerDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        if (token != null) {
            String email = jwtUtils.getUsernameFromToken(token);
            log.info("Email from token: {}", email);

            try {
                UserDetails userDetails = customerDetailsService.loadUserByUsername(email);
                log.info("User loaded from DB: {}", userDetails.getUsername());

                if (StringUtils.hasText(email) && jwtUtils.isTokenValid(token, userDetails)) {
                    if (!email.equals(userDetails.getUsername())) {
                        log.warn("Token username and DB username do not match. Token email: [{}], DB email: [{}]",
                                email, userDetails.getUsername());
                    } else {
                        log.info("Token is valid and matches user: {}", email);

                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        log.info("SecurityContext authentication set for user: {}", email);
                    }
                } else {
                    log.warn("Invalid token or token validation failed. Token: [{}], Email: [{}]", token, email);
                }

            } catch (Exception e) {
                log.error("Error loading user or validating token. Email: [{}], Exception: {}", email, e.getMessage());
            }
        } else {
            log.warn("Authorization header is missing or malformed");
        }

        filterChain.doFilter(request, response);
    }


    // lấy token từ header nếu có định dạng "Bearer <token>"
    private String getTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            log.info("Authorization Header: {}", token);
            String jwt = token.substring(7);
            log.info("Token after cut: {}", jwt);
            return jwt;
        }
        log.warn("No valid Authorization header found.");
        return null;
    }


}
