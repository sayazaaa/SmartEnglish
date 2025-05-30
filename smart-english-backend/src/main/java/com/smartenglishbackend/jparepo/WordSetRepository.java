package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.WordSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordSetRepository extends JpaRepository<WordSet, Integer> {
    abstract WordSet findByAccountId(Integer account_id);
}
