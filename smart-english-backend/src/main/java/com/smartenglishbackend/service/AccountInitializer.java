package com.smartenglishbackend.service;

import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jpaentity.FavoritesSet;
import com.smartenglishbackend.jpaentity.NWordBook;
import com.smartenglishbackend.jpaentity.WordSet;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.jparepo.FavoritesSetRepository;
import com.smartenglishbackend.jparepo.NWordBookRepository;
import com.smartenglishbackend.jparepo.WordSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountInitializer {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WordSetRepository wordSetRepository;
    @Autowired
    private FavoritesSetRepository favoritesSetRepository;
    @Autowired
    private NWordBookRepository nWordBookRepository;
    @Transactional
    public void AccountInit(Account account) {
        accountRepository.save(account);
        Account newAccount = accountRepository.findByPhone(account.getPhone());
        Integer newId = newAccount.getId();
        WordSet wordSet = new WordSet();
        wordSet.setAccountId(newId);
        wordSetRepository.save(wordSet);
        FavoritesSet favoritesSet = new FavoritesSet();
        favoritesSet.setAccountId(newId);
        favoritesSet.setName("默认");
        favoritesSet.setCover("");
        favoritesSetRepository.save(favoritesSet);
        NWordBook nWordBook = new NWordBook();
        nWordBook.setAccountId(newId);
        nWordBook.setName("默认");
        nWordBook.setCover("");
        nWordBookRepository.save(nWordBook);
    }
}
