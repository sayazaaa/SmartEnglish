package com.smartenglishbackend.jpaentity;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "accountoperator")
public class AccountOperator {
    @Id
    @Column(columnDefinition = "INT")
    private Integer id;
    @Column(columnDefinition = "INT", name="account_id")
    private Integer accountId;
    @Column(columnDefinition = "INT", name="op")
    private Integer op;
    @Column(columnDefinition = "DATE", name = "logdate")
    private LocalDate logDate;

}
