package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.AccountException;
import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTONWordBook;
import com.smartenglishbackend.dto.request.DTOUpdateNWordBook;
import com.smartenglishbackend.jpaentity.NWordBook;
import com.smartenglishbackend.jparepo.NWordBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NWordBookService {
    @Autowired
    private NWordBookRepository nWordBookRepository;
    public List<NWordBook> getNWordBooks(Integer userId) {
        List<NWordBook> nWordBooks = nWordBookRepository.findByAccountId(userId);
        for(NWordBook nWordBook : nWordBooks) {
            nWordBook.setContent(null);
            nWordBook.setAccountId(null);
        }
        return nWordBooks;
    }
    public List<String> GetNWordBookContent(Integer id) {
        Optional<NWordBook> nWordBook = nWordBookRepository.findById(id);
        if(nWordBook.isEmpty()){
            throw new MyResourceNotFoundException("NWordBook not found");
        }
        return nWordBook.get().getContent();
    }
    @Transactional
    public void CreateNWordBook(DTONWordBook nWordBook, Integer userId) {
        if(nWordBook.getName() == null || nWordBook.getCover() == null){
            throw new RequestFormatException("Invalid request");
        }
        NWordBook nWordBookEntity = new NWordBook();
        nWordBookEntity.setAccountId(userId);
        nWordBookEntity.setName(nWordBook.getName());
        nWordBookEntity.setCover(nWordBook.getCover());
        List<String> contents = new ArrayList<>();
        nWordBookEntity.setContent(contents);
        nWordBookRepository.save(nWordBookEntity);
    }
    @Transactional
    public void UpdateNWordBook(DTOUpdateNWordBook dtoUpdateNWordBook, Integer userId) {
        if(dtoUpdateNWordBook.getNWordBookId() == null || dtoUpdateNWordBook.getWord() == null ||
        dtoUpdateNWordBook.getMethod() == null){
            throw new RequestFormatException("Invalid request");
        }
        Optional<NWordBook> nWordBookOptional = nWordBookRepository.findById(dtoUpdateNWordBook.getNWordBookId());
        if(nWordBookOptional.isEmpty()){
            throw new MyResourceNotFoundException("NWordBook not found");
        }
        NWordBook nWordBookEntity = nWordBookOptional.get();
        if(nWordBookEntity.getContent() == null){
            nWordBookEntity.setContent(new ArrayList<>());
        }
        if(!nWordBookEntity.getAccountId().equals(userId)){
            throw new AccountException("Wrong account");
        }
        if(dtoUpdateNWordBook.getMethod().equals("add")){
            if(!nWordBookEntity.getContent().contains(dtoUpdateNWordBook.getWord())){
                nWordBookEntity.getContent().add(dtoUpdateNWordBook.getWord());
            }
        }else if(dtoUpdateNWordBook.getMethod().equals("delete")){
            nWordBookEntity.getContent().remove(dtoUpdateNWordBook.getWord());
        }else{
            throw new RequestFormatException("Invalid request");
        }
    }
    public Boolean checkNWordBook(Integer id, String word){
        if(id == null || word == null){
            throw new RequestFormatException("Invalid request");
        }
        Optional<NWordBook> nWordBookOptional = nWordBookRepository.findById(id);
        if(nWordBookOptional.isEmpty()){
            throw new MyResourceNotFoundException("NWordBook not found");
        }
        NWordBook nWordBookEntity = nWordBookOptional.get();
        return nWordBookEntity.getContent().contains(word);
    }
}
