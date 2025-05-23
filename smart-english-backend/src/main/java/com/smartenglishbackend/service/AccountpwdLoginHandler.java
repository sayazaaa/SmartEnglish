package com.smartenglishbackend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.dto.response.PDTOAccount;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jparepo.AccountReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountpwdLoginHandler implements IAccountHandler{

    @Autowired
    private AccountReposity accountReposity;
    @Autowired
    private Algorithm jwtAlgorithm;
    @Override
    public boolean accept(DTOAccount dtoAccount, String method) {
        return (method.equals("GET")
                && dtoAccount.getPassword() != null
                && dtoAccount.getPhone() != null
                && (!dtoAccount.getPhone().isEmpty())
                && dtoAccount.getVerification() == null);
    }

    @Override
    public ResponseEntity<PDTOAccount> Handle(DTOAccount dtoAccount) {
        if(accept(dtoAccount, "GET")){
            Account account = accountReposity.findByPhone(dtoAccount.getPhone());
            if(account == null){
                throw new AccountException("Account not found");
            }
            //验证密码
            String password = account.getPassword();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean acc = encoder.matches(dtoAccount.getPassword(), password);
            if(!acc){
                throw new AccountException("Wrong password");
            }
            //返回token
            String token = JWT.create()
                    .withSubject(String.valueOf(account.getId()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 3600*1000*24*7))
                    .sign(jwtAlgorithm);
            ResponseEntity<PDTOAccount> response = ResponseEntity.ok()
                    .header("Authorization",token)
                    .body(new PDTOAccount("Login succeed"));
            return response;
        }else{
            throw new RequestFormatException("Invalid Request");
        }
    }
}
