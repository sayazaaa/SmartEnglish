package com.smartenglishbackend.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.dto.response.PDTOAccount;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountVerificationLoginHandler implements IAccountHandler{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private VerificationSender verificationSender;
    @Autowired
    private Cache<String, Object> cache;
    @Autowired
    private UseDataLogger useDataLogger;
    @Override
    public boolean accept(DTOAccount dtoAccount, String method){
        return dtoAccount.getPhone() != null && (!dtoAccount.getPhone().isEmpty()) && method.equals("LOGIN")
                &&dtoAccount.getPassword() == null;
    }

    @Override
    public ResponseEntity<PDTOAccount> Handle(DTOAccount dtoAccount) {
        if(dtoAccount.getPhone() == null || dtoAccount.getPhone().isEmpty()){
            throw new RequestFormatException("Phone number is empty");
        }
        if(dtoAccount.getVerification() == null){
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
            useDataLogger.LogLogin(account.getId());
            String token = jwtUtils.generateToken(account.getId());
            //token装载入Authorization字段
            ResponseEntity<PDTOAccount> response = ResponseEntity.ok()
                    .header("Authorization",token)
                    .body(new PDTOAccount("Login succeed"));
            return response;
        }
    }
}
