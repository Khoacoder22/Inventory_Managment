package com.example.InventoryManagment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {

    //bien thoi gian token hết hạn
    private static final long EXPIRATION_TIME_IN_MILLISEC = 1000L * 60L * 60L * 24L * 30L * 6L;

    private SecretKey key;

    //khai baáo khóa bí mật
    @Value("${secretJwtString}")
    private String secretJwtString;

    // tạo chuoỗi khóa
    @PostConstruct
    private void init(){
        byte[] keyByte = secretJwtString.getBytes(StandardCharsets.UTF_8);
        this.key = new SecretKeySpec(keyByte, "HmacSHA256");
    }

    // taạo token mới
    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILLISEC))
                .signWith(key)
                .compact();
    }

    // lấy user tu token
    public String getUsernameFromToken(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return(username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
