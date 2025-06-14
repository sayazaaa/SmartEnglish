package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOUpdateLearned;
import com.smartenglishbackend.dto.response.PDTOLearned;
import com.smartenglishbackend.jpaentity.*;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.jparepo.LearnedRepository;
import com.smartenglishbackend.jparepo.WordSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LearnedService {
    @Autowired
    private LearnedRepository learnedRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WordGenerator wordGenerator;
    @Autowired
    private WordSetRepository wordSetRepository;
    @Transactional
    public String UpdateLearned(DTOUpdateLearned dtoUpdateLearned, Integer userId) {
        Optional<Learned> learnedOptional = learnedRepository.findById(new LearnedId(dtoUpdateLearned.getWord(), userId));
        Learned learned = learnedOptional.isPresent()?learnedOptional.get():(new Learned());
        learned.setWord(dtoUpdateLearned.getWord());
        learned.setReview_date(dtoUpdateLearned.getReviewDate());
        learned.setAccountId(userId);
        learnedRepository.save(learned);
        if(dtoUpdateLearned.getStatus().equals("learn")){
            Optional<Account> accountOptional = accountRepository.findById(userId);
            if(accountOptional.isEmpty()){
                throw new AccountException("Account not found");
            }
            Account account = accountOptional.get();
            Pair<String, Integer> res = wordGenerator.GetNewWord(userId, account.getWordbook_p());
            account.setWordbook_p(res.getSecond());
            WordSet wordSet = wordSetRepository.findByAccountId(account.getId());
            if(wordSet == null){
                throw new AccountException("Word set not found");
            }
            List<SWord> toRemove = wordSet.getSetpre().stream().
                    filter(sWord -> sWord.getWord().equals(dtoUpdateLearned.getWord())).toList();
            for (SWord sWord : toRemove) {
                wordSet.getSetpre().remove(sWord);
            }
            if(!res.getFirst().isEmpty()){
                wordSet.getSetpre().add(new SWord(res.getFirst(), 0));
            }
            if(learnedOptional.isEmpty())account.setNewWordCount(account.getNewWordCount() - 1);
            wordSetRepository.save(wordSet);
            accountRepository.save(account);
            return res.getFirst();
        }else if(dtoUpdateLearned.getStatus().equals("review")){
            List<String> words = wordGenerator.GetReviewWord(userId,1);
            WordSet wordSet = wordSetRepository.findByAccountId(userId);
            List<SWord> toRemove = wordSet.getSetlearned().stream().
                    filter(sWord -> sWord.getWord().equals(dtoUpdateLearned.getWord())).toList();
            for (SWord sWord : toRemove) {
                wordSet.getSetlearned().remove(sWord);
            }
            if(words.isEmpty())return "";
            else{
                for(String word : words){
                    wordSet.getSetlearned().add(new SWord(word, 0));
                }
            }
            wordSetRepository.save(wordSet);
            return words.getFirst();
        }else{
            throw new RequestFormatException("Invalid request");
        }
    }
    public List<PDTOLearned> getAllLearned(Integer userId){
        List<Learned> learnedList = learnedRepository.findByAccountId(userId);
        List<PDTOLearned> pDTOLearnedList = new ArrayList<>();
        for(Learned learned : learnedList){
            pDTOLearnedList.add(new PDTOLearned(learned.getWord(),learned.getReview_date()));
        }
        return pDTOLearnedList;
    }
}
