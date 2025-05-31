package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.WordBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordBookRepository extends JpaRepository<WordBook, Integer> {
    abstract WordBook findByName(String name);
}
