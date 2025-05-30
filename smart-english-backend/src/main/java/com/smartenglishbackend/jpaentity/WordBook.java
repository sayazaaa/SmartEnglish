package com.smartenglishbackend.jpaentity;

import com.smartenglishbackend.utils.JsonStringArrayConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="wordbook")
public class WordBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length=45)
    private String name;
    @Column(name="`describe`", columnDefinition = "TEXT")
    private String describe;
    @Column(length=128)
    private String cover;
    @Column(columnDefinition = "json")
    @Convert(converter = JsonStringArrayConverter.class)
    private List<String> content;
}
