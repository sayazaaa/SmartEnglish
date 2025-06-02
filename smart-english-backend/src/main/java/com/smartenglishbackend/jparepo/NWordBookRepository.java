package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.NWordBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NWordBookRepository extends JpaRepository<NWordBook, Integer> {
    List<NWordBook> findByAccountId(Integer accountId);
}
