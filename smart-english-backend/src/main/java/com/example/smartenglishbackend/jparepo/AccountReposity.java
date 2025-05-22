package com.example.smartenglishbackend.jparepo;

import com.example.smartenglishbackend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountReposity extends JpaRepository<Account, Long> {
    abstract Account findById(int id);
    abstract Account findByPhone(String phone);
    abstract void deleteById(int id);
}
