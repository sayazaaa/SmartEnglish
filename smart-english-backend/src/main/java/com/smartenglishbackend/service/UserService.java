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

import java.lang.reflect.Field;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WordBookRepository wordBookRepository;
    public ResponseEntity<PDTOUser> getUser(Integer userId) {
        Optional<Account> accountOptional = accountRepository.findById(userId);
        if (accountOptional.isEmpty()) {
            throw new AccountException("Account not found");
        }
        Account account = accountOptional.get();
        if(account.getWordbookId() != null){
            Optional<WordBook> wordBookOptional = wordBookRepository.findById(account.getWordbookId());
            if(wordBookOptional.isPresent()){
                WordBook wordBook = wordBookOptional.get();
                List<String> list = wordBook.getContent();
                PDTOWordBookBasic wordBookBasic = new PDTOWordBookBasic(wordBook.getId(), wordBook.getName(), wordBook.getCover(), list.size());
                System.out.println(wordBook);
                return ResponseEntity.ok(new PDTOUser(account.getName(), account.getDescription(), account.getAvatar(), wordBookBasic));
            }
        }
        return ResponseEntity.ok(new PDTOUser(account.getName(), account.getDescription(), account.getAvatar(), null));
    }

    public ResponseEntity<PDTOUser> updateUser(DTOUser dtoUser, Integer userId) {
        Optional<Account> accountOptional = accountRepository.findById(userId);
        if (accountOptional.isEmpty()) {
            throw new AccountException("Account not found");
        }
        Account account = accountOptional.get();
        try{
            Class<?> dtoClass = dtoUser.getClass();
            Class<?> accountClass = account.getClass();
            Field[] fileds = dtoClass.getDeclaredFields();
            for (Field field : fileds) {
                field.setAccessible(true);
                Object fieldValue = field.get(dtoUser);
                if (fieldValue != null) {
                    try{
                        Field accountField = accountClass.getDeclaredField(field.getName());
                        accountField.setAccessible(true);
                        accountField.set(account, fieldValue);
                    }catch (NoSuchFieldException e){
                        throw new RuntimeException("Field not found");
                    }
                }
            }
        }catch (IllegalAccessException e){
            throw new RuntimeException("IllegalAccessException");
        }
        accountRepository.save(account);
        PDTOUser pdtoUser = new PDTOUser(account.getName(), account.getDescription(), account.getAvatar(), null);
        if(account.getWordbookId() != null){
            Optional<WordBook> wordBookOptional = wordBookRepository.findById(account.getWordbookId());

            if(wordBookOptional.isPresent()){
                WordBook wordBook = wordBookOptional.get();
                List<String> list = wordBook.getContent();
                PDTOWordBookBasic pdtoWordBookBasic = new PDTOWordBookBasic(wordBook.getId(), wordBook.getName(), wordBook.getCover(), list.size());
                pdtoUser.setWordbook(pdtoWordBookBasic);
            }
        }
        return ResponseEntity.ok(pdtoUser);
    }
}
