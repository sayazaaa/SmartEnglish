package com.smartenglishbackend.jpaentity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name="learned")
@IdClass(LearnedId.class)
public class Learned {
    @Id
    private String word;
    @Id
    private Integer account_id;
    private Date review_date;
}
