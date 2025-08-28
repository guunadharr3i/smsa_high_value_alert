package com.smsa.highValueAlerts.repository;

import com.smsa.highValueAlerts.entity.SmsaAccountMaster;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsaAccountRepository extends JpaRepository<SmsaAccountMaster, Long> {
    Optional<SmsaAccountMaster> findByAccountNO(String accountNO);
    void deleteByAccountNO(String accountNO);
}
