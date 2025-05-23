package com.smartenglishbackend.service;

import com.smartenglishbackend.dto.request.DTOAccount;
import com.smartenglishbackend.jparepo.AccountReposity;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountReposity accountReposity;
    @Autowired
    private Cache<String, Object> cache;
    @Autowired
    private List<IAccountHandler> accountHandlers;
    public IAccountHandler getAccountHandler(DTOAccount dtoAccount, String method) {
        return accountHandlers.stream()
                .filter(h -> h.accept(dtoAccount, method))
                .findFirst()
                .orElseThrow(()->new RuntimeException("No handler found for " + dtoAccount));
    }
}
