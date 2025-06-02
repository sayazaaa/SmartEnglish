package com.smartenglishbackend.jpaentity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="favorites")
@IdClass(FavoritesId.class)
public class Favorites {
    @Id
    @Column(columnDefinition = "INT",name = "setid")
    private Integer setId;
    @Id
    @Column(length = 255, name = "articleid")
    private String articleId;
}
