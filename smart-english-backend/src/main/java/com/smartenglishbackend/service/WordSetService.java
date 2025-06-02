package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jpaentity.SWord;
import com.smartenglishbackend.jpaentity.WordSet;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.jparepo.WordSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WordSetService {
    @Autowired
    private WordSetRepository wordSetRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WordGenerator wordGenerator;
    @Transactional
    public List<SWord> getWordSet(String type, int userId) {
        WordSet wordSet = wordSetRepository.findByAccountId(userId);
        if (wordSet == null) {
            throw new AccountException("No such account");
        }
        Optional<Account> accountOptional = accountRepository.findById(userId);
        assert (accountOptional.isPresent());
        Account account = accountOptional.get();
        List<SWord> words;
        if(type.equals("review")){
            words = wordSet.getSetlearned();
        }else if (type.equals("learn")){
            words = wordSet.getSetpre();
        }else{
            throw new RequestFormatException("Invalid type");
        }
        if(words == null || words.size() < 10){
            if(words == null)words = new ArrayList<SWord>();
            if(type.equals("review")){
                List<String>res = wordGenerator.GetReviewWord(userId,10-words.size());
                for(String word : res){
                    words.add(new SWord(word,0));
                }
            }else{
                Integer finP = account.getWordbook_p();
                if(finP == null){finP = 0;}
                while(words.size() < 10){
                    String newWord;
                    Pair<String, Integer> res;
                    res = wordGenerator.GetNewWord(userId,finP);
                    finP = res.getSecond();
                    if(res.getFirst().isEmpty()){
                        break;
                    }
                    words.add(new SWord(res.getFirst(), 0));
                }
                account.setWordbook_p(finP);
                accountRepository.save(account);
            }
            if(type.equals("review")){
                wordSet.setSetlearned(words);
            }else{
                wordSet.setSetpre(words);
            }
            wordSetRepository.save(wordSet);
        }
        return words;
    }
    public boolean UpdateWordSet(List<SWord> wordSet, String type, int userId) {
        WordSet wordSet1 = wordSetRepository.findByAccountId(userId);
        if(wordSet1 == null){
            throw new AccountException("No such account");
        }
        if(type.equals("review")){
            wordSet1.setSetlearned(wordSet);
        }else if (type.equals("learn")){
            wordSet1.setSetpre(wordSet);
        }else{
            throw new RequestFormatException("Invalid type");
        }
        wordSetRepository.save(wordSet1);
        return true;
    }
}
