package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="favorites")
@IdClass(FavoritesId.class)
public class Favorites {
    @Id
    @Column(columnDefinition = "INT")
    private int setid;
    @Id
    @Column(length = 255)
    private String articleid;
}
