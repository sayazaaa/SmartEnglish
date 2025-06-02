package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    abstract List<Feedback> findByAccountId(int accountId);
}
