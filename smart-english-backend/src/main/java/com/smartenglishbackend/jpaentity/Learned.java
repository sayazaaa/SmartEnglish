package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

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
    private Date review_date;

}
