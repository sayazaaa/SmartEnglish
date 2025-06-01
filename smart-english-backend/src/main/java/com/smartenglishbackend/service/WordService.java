package com.smartenglishbackend.service;

import com.smartenglishbackend.customexceptions.MyResourceNotFoundException;
import com.smartenglishbackend.esentity.Word;
import com.smartenglishbackend.esrepo.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WordService {
    @Autowired
    private WordRepository wordRepository;
    public Word GetWordDetail(String word) {
        Word wordDetail = wordRepository.findByWord(word);
        if (wordDetail == null) {
            throw new MyResourceNotFoundException("Word not found");
        }
        return wordDetail;
    }
    public List<Word> SearchWords(String word) {
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Order.desc("_score")));
        return wordRepository.searchWordsByWord(word,pageable);
    }
    @Transactional
    public void SaveWord(Word word) {
        wordRepository.save(word);
    }
}
