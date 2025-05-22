package com.example.smartenglishbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Data
@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="wordbook_id", nullable = true)
    private Integer wordbookId;
    @Column(name="createdate")
    private Date createDate;
    @Column(length = 45)
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
