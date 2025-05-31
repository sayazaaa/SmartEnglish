package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.AccountOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountOperatorRepository extends JpaRepository<AccountOperator, Integer> {
    abstract List<AccountOperator> findByAccountId(Integer accountId);
    abstract List<AccountOperator> findByOp(Integer op);
}
