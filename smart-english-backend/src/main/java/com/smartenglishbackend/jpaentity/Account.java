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
    private Integer id;
    @Column(name="wordbook_id", nullable = true, columnDefinition = "INT")
    private Integer wordbookId;
    @Column(name="createdate",columnDefinition = "DATE")
    private LocalDate createDate;
    @Column(length = 45, columnDefinition = "VARCHAR(45)")
    private String phone;
    @Column(length = 128)
    private String password;
    @Column(length = 128)
    private String avatar;
    @Column(length = 45)
    private String nickname;
    @Column(name="`describe`",columnDefinition = "TEXT")
    private String describe;
}
