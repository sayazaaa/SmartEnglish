package com.smartenglishbackend.jpaentity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class FavoritesId implements Serializable {
    private Integer setId;
    private String articleId;
    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj == null || getClass() != obj.getClass())return false;
        FavoritesId other = (FavoritesId)obj;
        return setId.equals(other.setId) && articleId.equals(other.articleId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(setId,articleId);
    }
}
