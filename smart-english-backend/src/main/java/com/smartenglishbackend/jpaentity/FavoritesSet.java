package com.smartenglishbackend.jpaentity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="favorites_set")
public class FavoritesSet {
    @Id
    @Column(columnDefinition = "INT")
    private int id;
    @Column(columnDefinition = "INT")
    private int account_id;
    @Column(length = 64)
    private String name;
    @Column(length = 128)
    private String cover;
}
