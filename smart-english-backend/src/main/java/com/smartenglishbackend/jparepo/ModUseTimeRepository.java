package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.dto.ModUseTimeStatistics;
import com.smartenglishbackend.jpaentity.ModUseTime;
import com.smartenglishbackend.jpaentity.ModUseTimeId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ModUseTimeRepository extends JpaRepository<ModUseTime, ModUseTimeId> {

    @Query("""
select new com.smartenglishbackend.dto.ModUseTimeStatistics(
    modName,
    sum(useTime)
) from ModUseTime group by modName
""")
    List<ModUseTimeStatistics> GetSumUseTime();
    @Query("""
select new com.smartenglishbackend.dto.ModUseTimeStatistics(
    modName,
    avg(useTime)
) from ModUseTime group by modName
""")
    List<ModUseTimeStatistics> GetAvgUseTime();
}
