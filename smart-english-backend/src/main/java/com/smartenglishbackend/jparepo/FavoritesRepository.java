package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.Favorites;
import com.smartenglishbackend.jpaentity.FavoritesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, FavoritesId> {
    abstract List<Favorites> findBySetId(int setId);
}
