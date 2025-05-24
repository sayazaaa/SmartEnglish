package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.WordBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordBookRepository extends JpaRepository<WordBook, Long> {
    abstract WordBook findByName(String name);
    abstract WordBook findById(int id);
}
