package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;
    @Column(name="wordbook_id", nullable = true, columnDefinition = "INT")
    private Integer wordbookId;
    @Column(name="createdate",columnDefinition = "DATE")
    private LocalDate createDate;
    @Column(length = 45, columnDefinition = "VARCHAR")
    private String phone;
    @Column(length = 128)
    private String password;
    @Column(length = 128)
    private String avatar;
    @Column(name="nickname",length = 45)
    private String name;
    @Column(name="`describe`",columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "INT")
    private Integer wordbook_p;
    @Column(columnDefinition = "INT", name="new_word_count")
    private Integer newWordCount;
}
