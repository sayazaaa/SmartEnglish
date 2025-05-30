package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.FavoritesSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesSetRepository extends JpaRepository<FavoritesSet, Integer> {
    abstract List<FavoritesSet> findByAccountId(Integer id);
}
