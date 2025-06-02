package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    abstract Account findByPhone(String phone);
    abstract void deleteById(int id);
}
