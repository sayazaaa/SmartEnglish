package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
@Table(name="learned")
@IdClass(LearnedId.class)
public class Learned {
    @Id
    @Column(length=45)
    private String word;
    @Id
    @Column(columnDefinition = "INT",name = "account_id")
    private Integer accountId;
    @Column(columnDefinition = "DATE")
    private LocalDate review_date;
}
