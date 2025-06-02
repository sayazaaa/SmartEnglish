package com.smartenglishbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jparepo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Component
public class JWTUtils {
    @Autowired
    private Algorithm algorithm;
    @Autowired
    private AccountRepository accountRepository;
    @Value("${jwt.expiration-time}")
    private Long expirationTime;//单位为秒
    public String generateToken(Integer userId) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*expirationTime))
                .sign(algorithm);
    }
    public boolean verifyToken(String token) {
        if(token == null){
            return false;
        }
        try{
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(Integer.parseInt(decodedJWT.getSubject()) == -1)return true;
            Optional<Account> accountOptional = accountRepository.findById(Integer.parseInt(decodedJWT.getSubject()));
            return accountOptional.isPresent();
        }catch (JWTVerificationException e){
            return false;
        }
    }
    public Integer getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return Integer.parseInt(decodedJWT.getSubject());
    }
}
