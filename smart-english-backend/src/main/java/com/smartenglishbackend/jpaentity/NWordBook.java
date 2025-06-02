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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;
    @Column(columnDefinition = "INT", name = "account_id")
    private Integer accountId;
    @Column(length = 45)
    private String name;
    @Column(length = 128)
    private String cover;
    @Column(columnDefinition = "JSON")
    @Convert(converter = JsonStringArrayConverter.class)
    private List<String> content;
}
