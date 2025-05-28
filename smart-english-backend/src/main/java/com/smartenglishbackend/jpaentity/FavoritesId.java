package com.smartenglishbackend.jpaentity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class FavoritesId implements Serializable {
    private int setid;
    private String articleid;
    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj == null || getClass() != obj.getClass())return false;
        FavoritesId other = (FavoritesId)obj;
        return setid == other.setid && articleid.equals(other.articleid);
    }
    @Override
    public int hashCode() {
        return Objects.hash(setid,articleid);
    }
}
