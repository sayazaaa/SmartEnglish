package com.smartenglishbackend.jpaentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name="feedback")
public class Feedback {
    @Id
    @Column(columnDefinition = "INT")
    private int id;
    @Column(columnDefinition = "INT")
    private int account_id;
    @Column(columnDefinition = "DATE", name="`date`")
    private LocalDate date;
    @Column(columnDefinition = "TEXT")
    private String content;
}
