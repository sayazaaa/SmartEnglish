package com.smartenglishbackend.jpaentity;

import com.smartenglishbackend.utils.JsonArrayConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name="wordbook")
public class WordBook {
    @Id
    private Integer id;
    private String name;
    @Column(name="`describe`")
    private String describe;
    private String cover;
    @Column(columnDefinition = "json")
    @Convert(converter = JsonArrayConverter.class)
    private List<Object> content;
}
