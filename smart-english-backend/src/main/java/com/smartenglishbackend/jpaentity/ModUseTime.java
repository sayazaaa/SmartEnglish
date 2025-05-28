package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="modusetime")
@IdClass(ModUseTimeId.class)
public class ModUseTime {
    @Id
    @Column(length=45)
    private String modname;
    @Id
    @Column(columnDefinition = "INT")
    private int account_id;
    @Column(columnDefinition = "INT")
    private int usetime;
}
