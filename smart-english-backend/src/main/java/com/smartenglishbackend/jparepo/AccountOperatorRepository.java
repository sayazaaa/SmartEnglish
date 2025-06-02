package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.AccountOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountOperatorRepository extends JpaRepository<AccountOperator, Integer> {
    abstract List<AccountOperator> findByAccountId(Integer accountId);
    abstract List<AccountOperator> findByOp(Integer op);
    @Query("""
        select AccountOperator from AccountOperator where accountId = ?1 and logDate = ?2 and op = ?3
    """)
    List<AccountOperator> searchByAccountIdAndDateAndOp(Integer accountId, LocalDate date, Integer op);
}
