package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.Learned;
import com.smartenglishbackend.jpaentity.LearnedId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface LearnedRepository extends JpaRepository<Learned, LearnedId> {
    @Query(value="SELECT * from learned c WHERE c.review_date<?1 AND c.account_id=?2",nativeQuery = true)
    List<Learned> findTodayReview(LocalDate date, Integer id);
}
