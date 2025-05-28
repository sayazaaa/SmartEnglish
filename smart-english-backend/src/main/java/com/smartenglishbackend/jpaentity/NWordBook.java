package com.smartenglishbackend.jpaentity;

import com.smartenglishbackend.utils.JsonStringArrayConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

@Entity
@Data
@Table(name="nwordbook")
public class NWordBook {
    @Id
    @Column(columnDefinition = "INT")
    private int id;
    @Column(columnDefinition = "INT")
    private int account_id;
    @Column(length = 45)
    private String name;
    @Column(length = 128)
    private String cover;
    @Column(columnDefinition = "JSON")
    @Convert(converter = JsonStringArrayConverter.class)
    private List<String> content;
}
