package com.example.smartenglishbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.smartenglishbackend.dto.DTOAccount;
import com.example.smartenglishbackend.entity.Account;
import com.example.smartenglishbackend.jparepo.AccountReposity;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Service
public class AccountService {
    @Autowired
    private AccountReposity accountReposity;
    @Autowired
    private Cache<String, Object> cache;
    public void addAccount(Account account) {
        try{
            accountReposity.save(account);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    //检验是否已经注册
    public Boolean GetRegistered(DTOAccount dtoAccount) {
        Account res = accountReposity.findByPhone(dtoAccount.getPhone());
        return res != null;
    }
    private Account getAccount(DTOAccount dtoAccount) {
        Account res = accountReposity.findByPhone(dtoAccount.getPhone());
        if(res == null){
            res = new Account();
            res.setPhone(dtoAccount.getPhone());
            res.setPassword(new BCryptPasswordEncoder().encode(dtoAccount.getPassword()));
            res.setNickname(dtoAccount.getPhone());
            LocalDate localDate = LocalDate.now();
            res.setCreateDate(java.sql.Date.valueOf(localDate));
        }
        return res;
    }
    //获取验证码
    public String getVerification(DTOAccount dtoAccount) {
        String phone = dtoAccount.getPhone();
        Random random = new Random();
        int veri = random.nextInt(9000) + 1000;
        String strVeri = String.valueOf(veri);
        cache.put(phone, strVeri);
        return strVeri;
    }
    //比对验证码
    public Boolean checkVerification(DTOAccount dtoAccount) {
        String phone = dtoAccount.getPhone();
        String veri = (String)cache.getIfPresent(phone);
        if(veri != null){
            return (dtoAccount.getVerification().equals(veri));
        }
        return false;
    }
    public Boolean RegisterAccount(DTOAccount dtoAccount) {
        if(dtoAccount.getPhone() == null || dtoAccount.getPassword() == null || dtoAccount.getVerification() == null){
            throw new RuntimeException("Info lost!");
        }
        Account res = accountReposity.findByPhone(dtoAccount.getPhone());
        if(res != null){
            throw new RuntimeException("Account already exists!");
        }
        Account newAccount = getAccount(dtoAccount);
        try{
            accountReposity.save(newAccount);
        }catch(Exception e){
            throw new RuntimeException("Database error!" + e.getMessage());
        }
        return true;
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
