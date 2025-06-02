package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.BasicLogData;
import com.smartenglishbackend.jpaentity.AccountOperator;
import com.smartenglishbackend.jpaentity.ModUseTime;
import com.smartenglishbackend.jpaentity.ModUseTimeId;
import com.smartenglishbackend.jparepo.AccountOperatorRepository;
import com.smartenglishbackend.jparepo.ModUseTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UseDataLogger {
    @Autowired
    private AccountOperatorRepository accountOperatorRepository;
    @Autowired
    private ModUseTimeRepository modUseTimeRepository;
    public void LogLogin(Integer userId){
        List<AccountOperator> accountOperators = accountOperatorRepository.searchByAccountIdAndDateAndOp(userId,LocalDate.now(),1);
        if(accountOperators.isEmpty()){
            AccountOperator accountOperator = new AccountOperator();
            accountOperator.setAccountId(userId);
            accountOperator.setOp(1);
            accountOperator.setLogDate(LocalDate.now());
            accountOperatorRepository.save(accountOperator);
        }
    }
    public void LogRegister(Integer userId){
        AccountOperator accountOperator = new AccountOperator();
        accountOperator.setAccountId(userId);
        accountOperator.setOp(0);
        accountOperator.setLogDate(LocalDate.now());
        accountOperatorRepository.save(accountOperator);
    }
    @Transactional
    public void AddUseTime(Integer userId, String function, Long addition){
        if(userId == null || addition == null || function == null){
            throw new RequestFormatException("Invalid request");
        }
        Optional<ModUseTime> modUseTime = modUseTimeRepository.findById(new ModUseTimeId(function, userId));
        ModUseTime modUseTimeEntity = new ModUseTime(function,userId,0L);
        if(modUseTime.isPresent()){
            modUseTimeEntity = modUseTime.get();
        }
        modUseTimeEntity.setUseTime(modUseTimeEntity.getUseTime() + addition);
        modUseTimeRepository.save(modUseTimeEntity);
    }
    public BasicLogData GetBasicLogData(){
        return null;
    }
}
