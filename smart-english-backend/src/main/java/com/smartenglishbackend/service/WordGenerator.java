package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.jpaentity.*;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.jparepo.LearnedRepository;
import com.smartenglishbackend.jparepo.WordBookRepository;
import com.smartenglishbackend.jparepo.WordSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WordGenerator {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LearnedRepository learnedRepository;
    @Autowired
    private WordBookRepository wordBookRepository;
    @Autowired
    private WordSetRepository wordSetRepository;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Pair<String,Integer> GetNewWord(int userId, int nowP) {
        Optional<Account> accountOptional = accountRepository.findById(userId);

        if (accountOptional.isEmpty()) {
            throw new AccountException("Account not found");
        }
        Account account = accountOptional.get();
        Integer wordbookId = account.getWordbookId();
        if(wordbookId == null){
            throw new MyResourceNotFoundException("Wordbook not found");
        }
        Optional<WordBook> wordBookOptional = wordBookRepository.findById(wordbookId);
        if(wordBookOptional.isEmpty()){
            throw new MyResourceNotFoundException("WordBook content not found");
        }
        WordBook wordBook = wordBookOptional.get();
        List<String> words = wordBook.getContent();
        if(account.getWordbook_p() == null){
            account.setWordbook_p(0);
        }
        Integer wordP = nowP;
        try{
            while(words.get(wordP)!=null && learnedRepository.findById(new LearnedId(words.get(wordP),userId)).isPresent()){
                wordP++;
            }
            String newWord = words.get(wordP);
            if(newWord == null){newWord = "";}
            else wordP++;
            return Pair.of(newWord,wordP);
        } catch(IndexOutOfBoundsException e){
            return Pair.of("",wordP);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<String> GetReviewWord(int userId, int maxCount) {
        Optional<Account> accountOptional = accountRepository.findById(userId);
        if (accountOptional.isEmpty()) {
            throw new AccountException("Account not found");
        }
        Account account = accountOptional.get();
        WordSet wordset = wordSetRepository.findByAccountId(userId);
        if(wordset == null){
            throw new MyResourceNotFoundException("WordSet not found");
        }
        List<Learned> learnedList = learnedRepository.findTodayReview(LocalDate.now(),userId);
        List<String> reviewWords = new ArrayList<>();
        for(Learned learned : learnedList){
            boolean flag = false;
            for(SWord sWord: wordset.getSetlearned()){
                if(sWord.getWord().equals(learned.getWord())){
                    flag = true;
                    break;
                }
            }
            if(flag){
                continue;
            }
            reviewWords.add(learned.getWord());
            if(reviewWords.size() >= maxCount){
                break;
            }
        }
        return reviewWords;
    }
}
