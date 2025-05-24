package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.dto.response.PDTOAccount;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jparepo.AccountRepository;
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
    private AccountRepository accountRepository;
    @Autowired
    private Cache<String, Object> cache;
    @Autowired
    VerificationSender verificationSender;
    @Override
    public boolean accept(DTOAccount dtoAccount, String method) {
        return (method.equals("POST") && dtoAccount.getPhone() != null);
    }
    @Override
    public ResponseEntity<PDTOAccount> Handle(DTOAccount dtoAccount){
        if(dtoAccount.getPhone() == null || dtoAccount.getPhone().isEmpty()){
            throw new RequestFormatException("Phone number is empty");
        }
        if(dtoAccount.getVerification() != null){//第二次带验证码
            String veri = (String)cache.getIfPresent(dtoAccount.getPhone());
            if(veri == null){
                throw new RuntimeException("The verification code has expired or is no longer valid. Please request a new one.");
            }
            Account account = new Account();
            if(accountRepository.findByPhone(dtoAccount.getPhone()) != null){
                throw new AccountException("Account already exists");
            }
            if(veri.equals(dtoAccount.getVerification())){
                account.setPhone(dtoAccount.getPhone());
                account.setNickname(dtoAccount.getPhone());
                account.setPassword(new BCryptPasswordEncoder().encode(dtoAccount.getPassword()));
                account.setCreateDate(java.sql.Date.valueOf(LocalDate.now()));
                try{
                    accountRepository.save(account);
                }catch(DataIntegrityViolationException e){
                    throw new RuntimeException("Database Error: Data integrity violation");
                }
            }else{
                throw new AccountException("Verification code incorrect");
            }
            return ResponseEntity.ok(new PDTOAccount("Account created"));
        }else{//第一次请求验证码
            if(dtoAccount.getPhone() == null || dtoAccount.getPhone().isEmpty()){
                throw new RequestFormatException("Phone number is empty");
            }
            Random random = new Random();
            int veri = random.nextInt(9000) + 1000;
            String strVeri = String.valueOf(veri);
            if(!verificationSender.Send(strVeri, dtoAccount.getPhone())){
                throw new RuntimeException("Send Verification Failed");
            }
            cache.put(dtoAccount.getPhone(), strVeri);
            return ResponseEntity.ok(new PDTOAccount("Verification sent"));
        }
    }
}
