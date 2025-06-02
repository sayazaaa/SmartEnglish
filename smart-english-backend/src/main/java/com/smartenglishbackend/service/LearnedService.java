package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOUpdateLearned;
import com.smartenglishbackend.dto.response.PDTOLearned;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jpaentity.Learned;
import com.smartenglishbackend.jpaentity.LearnedId;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.jparepo.LearnedRepository;
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
            account.setNewWordCount(account.getNewWordCount()+1);
            accountRepository.save(account);
            return res.getFirst();
        }else if(dtoUpdateLearned.getStatus().equals("review")){
            List<String> words = wordGenerator.GetReviewWord(userId,1);
            if(words.isEmpty())return "";
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
