package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.customexceptions.RequestFormatException;
import com.smartenglishbackend.dto.request.DTOUpdateWordBook;
import com.smartenglishbackend.dto.request.DTOWordBook;
import com.smartenglishbackend.dto.response.PDTOWordBook;
import com.smartenglishbackend.jpaentity.Account;
import com.smartenglishbackend.jpaentity.Learned;
import com.smartenglishbackend.jpaentity.WordBook;
import com.smartenglishbackend.jparepo.AccountRepository;
import com.smartenglishbackend.jparepo.LearnedRepository;
import com.smartenglishbackend.jparepo.WordBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class WordBookService {
    @Autowired
    private WordBookRepository wordBookRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private LearnedRepository learnedRepository;
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
    public List<PDTOWordBook> GetAllWordBook(){
        List<WordBook> wordBooks = wordBookRepository.findAll();
        List<PDTOWordBook> dtoWordBooks = new ArrayList<>();
        for (WordBook wordBook : wordBooks) {
            PDTOWordBook dtoWordBook = new PDTOWordBook();
            dtoWordBook.setId(wordBook.getId());
            dtoWordBook.setName(wordBook.getName());
            dtoWordBook.setCover(wordBook.getCover());
            dtoWordBook.setWordCount(wordBook.getContent().size());
            dtoWordBooks.add(dtoWordBook);
        }
        return dtoWordBooks;
    }
    public WordBook GetWordBookDetails(Integer id){
        Optional<WordBook> wordBookOptional = wordBookRepository.findById(id);
        if(wordBookOptional.isEmpty()) throw new MyResourceNotFoundException("Word Book Not Found");
        return wordBookOptional.get();
    }
    public void UpdateWordBook(DTOUpdateWordBook dtoWordBook) {
        if(dtoWordBook.getId() == null || dtoWordBook.getWord() == null || dtoWordBook.getMethod() == null){
            throw new RequestFormatException("Invalid Request");
        }
        Optional<WordBook> wordBookOptional = wordBookRepository.findById(dtoWordBook.getId());
        if(wordBookOptional.isEmpty()) throw new MyResourceNotFoundException("Word Book Not Found");
        WordBook wordBook = wordBookOptional.get();
        if(dtoWordBook.getMethod().equals("add")){
            if(!wordBook.getContent().contains(dtoWordBook.getWord())) {
                wordBook.getContent().add(dtoWordBook.getWord());
            }
        }else if(dtoWordBook.getMethod().equals("delete")){
            wordBook.getContent().remove(dtoWordBook.getWord());
        }else{
            throw new RequestFormatException("Invalid Request");
        }
        wordBookRepository.save(wordBook);
    }
    public void DeleteWordBook(Integer id) {
        Optional<WordBook> wordBookOptional = wordBookRepository.findById(id);
        if(wordBookOptional.isEmpty()) throw new MyResourceNotFoundException("Word Book Not Found");
        wordBookRepository.deleteById(id);
    }
    public List<String> GetNewWord20(Integer userId) {
        Optional<Account> accountOptional = accountRepository.findById(userId);
        assert accountOptional.isPresent();
        Account account = accountOptional.get();
        if(account.getWordbookId() == null){
            throw new MyResourceNotFoundException("Wordbook_p Not Found");
        }
        Optional<WordBook> wordBookOptional = wordBookRepository.findById(account.getWordbookId());
        if(wordBookOptional.isEmpty()) throw new MyResourceNotFoundException("Wordbook_p Not Found");
        WordBook wordBook = wordBookOptional.get();
        List<Learned> learnedWords = learnedRepository.findByAccountId(userId);
        List<String> learnedWordsString = learnedWords.stream().map(Learned::getWord).toList();
        List<String> newWords = wordBook.getContent().stream().
                filter(word -> !learnedWordsString.contains(word)).toList();
        return RandomSample20(newWords);
    }
    private List<String> RandomSample20(List<String> words) {
        int size = words.size();
        if(size <= 20)return words;
        List<String> res = new ArrayList<>();
        int p = 0;
        for(int i = 1; i <= 20; i++){
            Random r = new Random();
            if(size-20-i-p <= 0){
                res.add(words.get(p));
                p++;
                continue;
            }
            int index = r.nextInt(size-20-i-p) + p;
            res.add(words.get(index));
            p = index+1;
        }
        return res;
    }
}
