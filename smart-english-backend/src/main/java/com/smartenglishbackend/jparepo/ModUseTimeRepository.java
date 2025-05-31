package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.ModUseTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModUseTimeRepository extends JpaRepository<ModUseTime, Integer> {
}
