package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.dto.request.DTOUser;
import com.smartenglishbackend.dto.response.PDTOUser;
import com.smartenglishbackend.dto.response.PDTOWordBookBasic;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jpaentity.WordBook;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.jparepo.WordBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WordBookRepository wordBookRepository;
    public ResponseEntity<PDTOUser> getUser(Integer userId) {
        Account account = accountRepository.findById(userId);
        if (account == null) {
            throw new AccountException("Account not found");
        }
        if(account.getWordbookId() != null){
            WordBook wordBook = wordBookRepository.findById(account.getWordbookId());
            if(wordBook != null){
                List<String> list = wordBook.getContent();
                PDTOWordBookBasic wordBookBasic = new PDTOWordBookBasic(wordBook.getId(), wordBook.getName(), wordBook.getCover(), list.size());
                System.out.println(wordBook);
                return ResponseEntity.ok(new PDTOUser(account.getNickname(), account.getDescribe(), account.getAvatar(), wordBookBasic));
            }
        }
        return ResponseEntity.ok(new PDTOUser(account.getNickname(), account.getDescribe(), account.getAvatar(), null));
    }
    public ResponseEntity<PDTOUser> updateUser(DTOUser dtoUser, Integer userId) {

        return null;
    }
}
