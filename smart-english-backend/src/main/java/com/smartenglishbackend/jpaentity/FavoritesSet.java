package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="favorites_set")
public class FavoritesSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;
    @Column(columnDefinition = "INT", name = "account_id")
    private Integer accountId;
    @Column(length = 64)
    private String name;
    @Column(length = 128)
    private String cover;
}
