package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.jpaentity.AccountOperator;
import com.smartenglishbackend.jparepo.AccountOperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalcDataUserCount implements ICalcData{
    @Autowired
    private AccountOperatorRepository accountOperatorRepository;
    @Override
    public boolean accept(String method) {
        return method.equals("user_count");
    }

    @Override
    public List<Float> calc(LocalDate startTime, String duration) {
        LocalDate endDate = switch (duration){
            case "d" -> startTime.plusDays(1);
            case "w" -> startTime.plusWeeks(1);
            case "m" -> startTime.plusMonths(1);
            default -> throw new RequestFormatException("Invalid duration");
        };
        List<AccountOperator> accountOperators = accountOperatorRepository.
                searchByDateAndOp(startTime,endDate,0);

        return List.of((float)accountOperators.size());
    }
}
