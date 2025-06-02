package com.smartenglishbackend.esentity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(indexName="articles")
public class Article {
    @Id
    @Field(name="_id")
    private String id;
    private String title;
    private String cover;
    private String content;
    @Field(type = FieldType.Date)
    private LocalDate date;
    private List<String> tags;
}
