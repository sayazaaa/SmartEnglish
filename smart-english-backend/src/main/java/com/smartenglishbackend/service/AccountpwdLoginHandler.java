package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.dto.response.PDTOAccount;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountpwdLoginHandler implements IAccountHandler{

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UseDataLogger useDataLogger;
    @Override
    public boolean accept(DTOAccount dtoAccount, String method) {
        return (method.equals("LOGIN")
                && dtoAccount.getPassword() != null
                && dtoAccount.getPhone() != null
                && (!dtoAccount.getPhone().isEmpty())
                && dtoAccount.getVerification() == null);
    }

    @Override
    public ResponseEntity<PDTOAccount> Handle(DTOAccount dtoAccount) {
        if(accept(dtoAccount, "LOGIN")){
            Account account = accountRepository.findByPhone(dtoAccount.getPhone());
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
            useDataLogger.LogLogin(account.getId());
            String token = jwtUtils.generateToken(account.getId());
            ResponseEntity<PDTOAccount> response = ResponseEntity.ok()
                    .header("Authorization",token)
                    .body(new PDTOAccount("Login succeed"));
            return response;
        }else{
            throw new RequestFormatException("Invalid Request");
        }
    }
}
