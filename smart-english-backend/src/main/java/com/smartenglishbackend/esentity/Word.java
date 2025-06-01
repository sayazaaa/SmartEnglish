package com.smartenglishbackend.esentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Document(indexName = "words")
public class Word {
    @Id
    private String id;
    private String word;
    private String phonetic;
    private String pronunciation;
    private List<String> explanations;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RelatedWords{
        private List<String> a;
        private List<String> v;
        private List<String> n;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Example{
        private String english;
        private String chinese;
        private String audio;
    }
    private RelatedWords synonyms;
    private RelatedWords antonyms;
    private List<Example> examples;
}
