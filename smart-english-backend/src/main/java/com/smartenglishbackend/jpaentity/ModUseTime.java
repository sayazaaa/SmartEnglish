package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="modusetime")
@IdClass(ModUseTimeId.class)
public class ModUseTime {
    @Id
    @Column(length=45, name = "modname")
    private String modName;
    @Id
    @Column(columnDefinition = "INT", name = "account_id")
    private Integer accountId;
    @Column(columnDefinition = "INT", name = "usetime")
    private Integer useTime;
}
