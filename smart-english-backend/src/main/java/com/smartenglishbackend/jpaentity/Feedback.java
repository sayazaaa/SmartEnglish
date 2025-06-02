package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name="feedback")
public class Feedback {
    @Id
    @Column(columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "INT",name = "account_id")
    private Integer accountId;
    @Column(columnDefinition = "DATE", name="`date`")
    private LocalDate date;
    @Column(columnDefinition = "TEXT")
    private String content;
}
