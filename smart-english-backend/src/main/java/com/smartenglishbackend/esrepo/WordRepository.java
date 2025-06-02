package com.smartenglishbackend.esrepo;

import com.smartenglishbackend.esentity.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WordRepository extends CrudRepository<Word, String> {

    @Query("""
            {
                "match": {
                    "word": {
                        "query":"?0"
                    }
                }
            }
            """)
    List<Word> searchWordsByWord(String word, Pageable pageable);
    Word findByWord(String word);
}
