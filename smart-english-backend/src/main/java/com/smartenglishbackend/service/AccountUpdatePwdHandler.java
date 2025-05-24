package com.smartenglishbackend.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.dto.response.PDTOAccount;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jparepo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class AccountUpdatePwdHandler implements IAccountHandler {
    @Autowired
    private VerificationSender verificationSender;
    @Autowired
    private Cache<String, Object> cache;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public boolean accept(DTOAccount dtoAccount, String method) {
        return method.equals("PUT") && dtoAccount.getPhone() != null && (!dtoAccount.getPhone().isEmpty());
    }

    @Override
    @Transactional
    public ResponseEntity<PDTOAccount> Handle(DTOAccount dtoAccount) {
        if (dtoAccount.getVerification() == null) {
            Random random = new Random();
            int veri = random.nextInt(9000) + 1000;
            String strVeri = String.valueOf(veri);
            boolean status = verificationSender.Send(strVeri, dtoAccount.getPhone());
            if(!status){
                throw new RuntimeException("Verification send failed");
            }
            cache.put(dtoAccount.getPhone(), strVeri);
            return ResponseEntity.ok(new PDTOAccount("Verification send succeed"));
        }else{
            if(dtoAccount.getPassword() == null || dtoAccount.getPassword().isEmpty()) {
                throw new RequestFormatException("Password is empty");
            }
            String veri = (String)cache.getIfPresent(dtoAccount.getPhone());
            //找不到验证码，可能过期或还未请求
            if(veri == null){
                throw new RuntimeException("The verification code has expired or is no longer valid. Please request a new one.");
            }
            if(!veri.equals(dtoAccount.getVerification())){
                throw new AccountException("Verification code incorrect");
            }
            Account account = accountRepository.findByPhone(dtoAccount.getPhone());
            if(account == null){
                throw new AccountException("Account not found");
            }
            String newPwd = new BCryptPasswordEncoder().encode(dtoAccount.getPassword());
            account.setPassword(newPwd);
            return ResponseEntity.ok(new PDTOAccount("update pwd success"));
        }
    }
}
