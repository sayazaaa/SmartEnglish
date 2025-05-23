package com.smartenglishbackend.jparepo;

import com.smartenglishbackend.jpaentity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountReposity extends JpaRepository<Account, Long> {
    abstract Account findById(int id);
    abstract Account findByPhone(String phone);
    abstract void deleteById(int id);
}
