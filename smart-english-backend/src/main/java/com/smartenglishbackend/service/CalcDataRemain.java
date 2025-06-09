package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.jpaentity.AccountOperator;
import com.smartenglishbackend.jparepo.AccountOperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CalcDataRemain implements ICalcData{
    @Autowired
    private AccountOperatorRepository accountOperatorRepository;
    @Override
    public boolean accept(String method) {
        return method.equals("remain");
    }

    @Override
    public List<Float> calc(LocalDate startTime, String duration) {
        LocalDate endDate = switch (duration) {
            case "d" -> startTime.plusDays(1);
            case "w" -> startTime.plusWeeks(1);
            case "m" -> startTime.plusMonths(1);
            default -> throw new RequestFormatException();
        };
        List<AccountOperator> accountOperators = accountOperatorRepository.
                searchByDateAndOp(startTime,endDate,0);
        Float total = (float)accountOperators.size();
        int resSize = switch (duration){
            case "d" -> 7;
            case "w" -> 4;
            case "m" -> 12;
            default -> throw new RequestFormatException();
        };
        int add = switch (duration){
            case "d" -> 1;
            case "w" -> 7;
            case "m" -> 30;
            default -> throw new RequestFormatException();
        };
        if(total.equals(0f)){
            return Stream.generate(()->0f).limit(resSize).toList();
        }
        int offset = add;
        List<Float> res = new ArrayList<>();
        for(int i = 0; i < resSize; i++){
            float loginTotal = 0f;
            LocalDate temp = startTime;
            while(temp.isBefore(endDate)){
                LocalDate finTemp = temp;
                Long loginOperators = accountOperatorRepository.
                        searchByAccountIdAndDurationAndOp(accountOperators.stream().
                                filter(accountOperator -> accountOperator.getLogDate().equals(finTemp)).
                                        map(AccountOperator::getAccountId).toList(),temp.plusDays(offset),
                                temp.plusDays(offset+add),1);
                if(loginOperators != null)loginTotal += loginOperators;
                temp = temp.plusDays(1);
            }
            res.add(loginTotal/total);
            offset += add;
        }
        return res;
    }
}
