package com.smsa.highValueAlerts.repository;

import com.smsa.highValueAlerts.entity.SmsaAccountTemp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsaAccountTempRepo extends JpaRepository<SmsaAccountTemp, Long>{
    Optional<SmsaAccountTemp> findByAccountNO(String accountNO);
    void deleteByAccountNO(String accountNO);
}
