package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.BasicLogData;
import com.smartenglishbackend.dto.ModUseTimeStatistics;
import com.smartenglishbackend.dto.PDTOModUseTimeStatistics;
import com.smartenglishbackend.dto.request.DTOCalcDataReq;
import com.smartenglishbackend.jpaentity.AccountOperator;
import com.smartenglishbackend.jpaentity.ModUseTime;
import com.smartenglishbackend.jpaentity.ModUseTimeId;
import com.smartenglishbackend.jparepo.AccountOperatorRepository;
import com.smartenglishbackend.jparepo.ModUseTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.datatransfer.FlavorEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UseDataLogger {
    @Autowired
    private AccountOperatorRepository accountOperatorRepository;
    @Autowired
    private ModUseTimeRepository modUseTimeRepository;
    @Autowired
    private List<ICalcData> calcDataList;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void LogLoginWithDate(Integer userId, LocalDate date){
        List<AccountOperator> accountOperators = accountOperatorRepository.searchByAccountIdAndDateAndOp(userId,LocalDate.now(),1);
        if(accountOperators.isEmpty()){
            AccountOperator accountOperator = new AccountOperator();
            accountOperator.setAccountId(userId);
            accountOperator.setOp(1);
            accountOperator.setLogDate(date);
            accountOperatorRepository.save(accountOperator);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void LogRegister(Integer userId){
        AccountOperator accountOperator = new AccountOperator();
        accountOperator.setAccountId(userId);
        accountOperator.setOp(0);
        accountOperator.setLogDate(LocalDate.now());
        accountOperatorRepository.save(accountOperator);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void LogRegisterWithDate(Integer userId, LocalDate date){
        AccountOperator accountOperator = new AccountOperator();
        accountOperator.setAccountId(userId);
        accountOperator.setOp(0);
        accountOperator.setLogDate(date);
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
        List<AccountOperator> accountOperators = accountOperatorRepository.findByOp(0);
        BasicLogData basicLogData = new BasicLogData();
        basicLogData.setTotalUser(accountOperators.size());
        basicLogData.setTotalUseTime(MakePDTOModUseTimeStatistics(modUseTimeRepository.GetSumUseTime()));
        basicLogData.setAverageUseTime(MakePDTOModUseTimeStatistics(modUseTimeRepository.GetAvgUseTime()));
        return basicLogData;
    }
    private List<PDTOModUseTimeStatistics> MakePDTOModUseTimeStatistics(List<ModUseTimeStatistics> in){
        List<String> need = List.of("learn","review","listen","read");
        List<PDTOModUseTimeStatistics> out = new ArrayList<>();
        for(String item: need){
            boolean flag = false;
            for(ModUseTimeStatistics stat: in){
                if(stat.getModName().equals(item)){
                    out.add(new PDTOModUseTimeStatistics(stat));
                    flag = true;
                    break;
                }
            }
            if(!flag){
                out.add(new PDTOModUseTimeStatistics(item,0L));
            }
        }
        return out;
    }
    public List<Float> GetCalcData(DTOCalcDataReq dtoCalcDataReq){
        ICalcData calcData = calcDataList.stream().
                filter(c->c.accept(dtoCalcDataReq.getData())).
                findFirst().orElseThrow(()->new RequestFormatException("Invalid request"));
        return calcData.calc(dtoCalcDataReq.getStart(), dtoCalcDataReq.getType());
    }
    public Long GetModUseTime(Integer userId, String function){
        if(userId == null || function == null){
            throw new RequestFormatException("Invalid request");
        }
        Optional<ModUseTime> modUseTime = modUseTimeRepository.findById(new ModUseTimeId(function, userId));
        if(modUseTime.isEmpty()){
            return 0L;
        }
        return modUseTime.get().getUseTime();
    }
}
