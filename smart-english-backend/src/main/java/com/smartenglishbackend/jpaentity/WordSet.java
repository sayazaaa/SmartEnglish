package com.smartenglishbackend.jpaentity;

import com.smartenglishbackend.utils.JsonArrayConverter;
import com.smartenglishbackend.utils.JsonSWordArrayConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Table(name="wordset")
@Data
public class WordSet {
    @Id
    @Column(columnDefinition = "INT")
    private int account_id;
    @Column(columnDefinition = "JSON")
    @Convert(converter = JsonSWordArrayConverter.class)
    private List<SWord> setpre;
    @Column(columnDefinition = "JSON")
    @Convert(converter = JsonSWordArrayConverter.class)
    private List<SWord> setlearned;
}
