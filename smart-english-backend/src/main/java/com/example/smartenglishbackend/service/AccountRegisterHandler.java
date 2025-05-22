package com.example.smartenglishbackend.service;

import com.example.smartenglishbackend.dto.request.DTOAccount;
import com.example.smartenglishbackend.dto.response.PDTOAccount;
import com.example.smartenglishbackend.entity.Account;
import com.example.smartenglishbackend.jparepo.AccountReposity;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class AccountRegisterHandler implements IAccountHandler{
    @Autowired
    private AccountReposity accountReposity;
    @Autowired
    private Cache<String, Object> cache;
    @Override
    public boolean accept(DTOAccount dtoAccount, String method) {
        return (method.equals("POST") && dtoAccount.getPhone() != null);
    }
    @Override
    public ResponseEntity<PDTOAccount> Handle(DTOAccount dtoAccount){
        if(dtoAccount.getPhone() == null || dtoAccount.getPhone().isEmpty()){
            throw new RuntimeException("Phone number is empty");
        }
        if(accountReposity.findByPhone(dtoAccount.getPhone()) != null){
            throw new RuntimeException("Account already exists");
        }
        if(dtoAccount.getVerification() != null){//第二次带验证码
            String veri = (String)cache.getIfPresent(dtoAccount.getPhone());
            Account account = new Account();
            if(veri!=null && veri.equals(dtoAccount.getVerification())){
                account.setPhone(dtoAccount.getPhone());
                account.setAvatar("");
                account.setNickname(dtoAccount.getPhone());
                account.setPassword(new BCryptPasswordEncoder().encode(dtoAccount.getPassword()));
                account.setCreateDate(java.sql.Date.valueOf(LocalDate.now()));
                try{
                    accountReposity.save(account);
                }catch(DataIntegrityViolationException e){
                    throw new RuntimeException("Database Error: Data integrity violation");
                }
            }else{
                throw new RuntimeException("Verification Failed");
            }
            return ResponseEntity.ok(new PDTOAccount("Account created"));
        }else{//第一次请求验证码
            if(dtoAccount.getPhone() == null || dtoAccount.getPhone().isEmpty()){
                throw new RuntimeException("Phone number is empty");
            }
            Random random = new Random();
            int veri = random.nextInt(9000) + 1000;
            String strVeri = String.valueOf(veri);
            cache.put(dtoAccount.getPhone(), strVeri);
            return ResponseEntity.ok(new PDTOAccount(strVeri));
        }
    }
}
