package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="modusetime")
@IdClass(ModUseTimeId.class)
@AllArgsConstructor
@NoArgsConstructor
public class ModUseTime {
    @Id
    @Column(length=45, name = "modname")
    private String modName;
    @Id
    @Column(columnDefinition = "INT", name = "account_id")
    private Integer accountId;
    @Column(columnDefinition = "BIGINT", name = "usetime")
    private Long useTime;
}
