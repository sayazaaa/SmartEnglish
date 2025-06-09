package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.dto.request.DTOUser;
import com.smartenglishbackend.dto.response.PDTOUser;
import com.smartenglishbackend.dto.response.PDTOWordBookBasic;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jpaentity.Learned;
import com.smartenglishbackend.jpaentity.WordBook;
import com.smartenglishbackend.jpaentity.WordSet;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.jparepo.LearnedRepository;
import com.smartenglishbackend.jparepo.WordBookRepository;
import com.smartenglishbackend.jparepo.WordSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WordBookRepository wordBookRepository;
    @Autowired
    private LearnedRepository learnedRepository;
    @Autowired
    private WordSetRepository wordSetRepository;
    public ResponseEntity<PDTOUser> getUser(Integer userId) {
        Optional<Account> accountOptional = accountRepository.findById(userId);
        if (accountOptional.isEmpty()) {
            throw new AccountException("Account not found");
        }
        Account account = accountOptional.get();
        List<Learned> learnedList = learnedRepository.findTodayReview(LocalDate.now(),userId);
        PDTOUser pdtoUser = new PDTOUser(account.getName(), account.getDescription(), account.getAvatar(), null,
                account.getNewWordCount()==null?0:account.getNewWordCount(),learnedList.size());
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
    @Transactional
    public ResponseEntity<PDTOUser> updateUser(DTOUser dtoUser, Integer userId) {
        Optional<Account> accountOptional = accountRepository.findById(userId);
        if (accountOptional.isEmpty()) {
            throw new AccountException("Account not found");
        }
        Account account = accountOptional.get();
        if(dtoUser.getWordbookId() != null && !dtoUser.getWordbookId().equals(account.getWordbookId())){
            account.setWordbook_p(0);
            List<Learned> learnedList = learnedRepository.findByAccountId(account.getId());
            Optional<WordBook> wordBookOptional = wordBookRepository.findById(dtoUser.getWordbookId());
            if(wordBookOptional.isEmpty()){
                throw new MyResourceNotFoundException("Wordbook not found");
            }
            WordBook wordBook = wordBookOptional.get();
            List<String> list = wordBook.getContent();
            List<String> newList = new ArrayList<>();
            for(Learned learnedItem : learnedList){
                newList.add(learnedItem.getWord());
            }
            List<String> result = list.stream().filter(e->!newList.contains(e)).toList();
            WordSet wordSet = wordSetRepository.findByAccountId(account.getId());
            if(wordSet == null){
                throw new MyResourceNotFoundException("WordSet not found");
            }
            wordSet.getSetpre().clear();
            account.setNewWordCount(result.size());
        }
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
        List<Learned> learnedList = learnedRepository.findByAccountId(account.getId());
        List<Learned> filtered = learnedList.stream().filter(learned -> learned.getReview_date().isBefore(LocalDate.now())).toList();
        PDTOUser pdtoUser = new PDTOUser(account.getName(), account.getDescription(), account.getAvatar(), null,
                account.getNewWordCount()==null?0:account.getNewWordCount(),filtered.size());
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
