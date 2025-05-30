package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOWordBook;
import com.smartenglishbackend.jpaentity.WordBook;
import com.smartenglishbackend.jparepo.WordBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WordBookService {
    @Autowired
    private WordBookRepository wordBookRepository;
    @Transactional
    public void CreateWordBook(DTOWordBook dtoWordBook) {
        if(dtoWordBook.getContent() == null || dtoWordBook.getName() == null ||
        dtoWordBook.getCover() == null){
            throw new RequestFormatException("Invalid Request");
        }
        WordBook wordBook = new WordBook();
        wordBook.setName(dtoWordBook.getName());
        wordBook.setCover(dtoWordBook.getCover());
        wordBook.setContent(dtoWordBook.getContent());
        wordBookRepository.save(wordBook);
    }
}
