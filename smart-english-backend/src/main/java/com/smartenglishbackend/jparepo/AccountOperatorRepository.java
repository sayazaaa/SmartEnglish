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
    @Query(value = """
        select * from accountoperator ao 
        where ao.account_id = ?1 and ao.logdate = ?2 and ao.op = ?3
    """, nativeQuery = true)
    List<AccountOperator> searchByAccountIdAndDateAndOp(Integer accountId, LocalDate date, Integer op);
    @Query(value = """
        select * from accountoperator ao
        where ao.logdate >= ?1
        and ao.logdate < ?2
        and ao.op = ?3
    """,nativeQuery = true)
    List<AccountOperator> searchByDateAndOp(LocalDate start, LocalDate end, Integer op);
    @Query(value = """
        select count(DISTINCT ao.account_id) from accountoperator ao
        where ao.account_id in ?1
        and ao.logdate >= ?2
        and ao.logdate < ?3
        and ao.op = ?4 group by ao.account_id
    """,nativeQuery = true)
    Long searchByAccountIdAndDurationAndOp(List<Integer> accountId, LocalDate start, LocalDate end, Integer op);
}
