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
    public List<Word> GetAllWords() {
        Iterable<Word> words = wordRepository.findAll();
        List<Word> wordList = new ArrayList<>();
        for (Word word : words) {
            wordList.add(word);
        }
        return wordList;
    }
    public List<Word> SearchWordsFuzzy(String word) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("_score")));
        List<Word> wordList = new ArrayList<>();
        String searchWord = word;
        while (wordList.size() < 5) {
            if(searchWord.isEmpty()){
                wordList = GetAllWords().stream().
                        filter(w -> (!w.getExplanations().isEmpty())&&(!w.getWord().equals(word))).
                        toList();
                break;
            }
            wordList = wordRepository.searchWordsByWordFuzzy(searchWord,pageable);
            wordList = wordList.stream().
                    filter(w -> (!w.getExplanations().isEmpty())&&(!w.getWord().equals(word))).
                    toList();
            searchWord = searchWord.substring(0, searchWord.length()/2);
        }
        return wordList.subList(0,5).stream().toList();
    }
}
