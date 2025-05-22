package com.example.smartenglishbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.smartenglishbackend.dto.request.DTOAccount;
import com.example.smartenglishbackend.entity.Account;
import com.example.smartenglishbackend.jparepo.AccountReposity;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountReposity accountReposity;
    @Autowired
    private Cache<String, Object> cache;
    @Autowired
    private List<IAccountHandler> accountHandlers;
    public IAccountHandler getAccountHandler(DTOAccount dtoAccount, String method) {
        return accountHandlers.stream()
                .filter(h -> h.accept(dtoAccount, method))
                .findFirst()
                .orElseThrow(()->new RuntimeException("No handler found for " + dtoAccount));
    }
    //账密登录
    public String LoginAccount(DTOAccount dtoAccount) {
        Account res = accountReposity.findByPhone(dtoAccount.getPhone());
        if(res == null){
            throw new RuntimeException("Account not found!");
        }
        boolean check = new BCryptPasswordEncoder().matches(
                dtoAccount.getPassword(), res.getPassword()
        );
        if(!check){
            throw new RuntimeException("Wrong password!");
        }
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String token = JWT.create()
                .withSubject(dtoAccount.getPhone())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600*1000*24*7))//一周过期
                .sign(algorithm);
        return token;
    }
}
