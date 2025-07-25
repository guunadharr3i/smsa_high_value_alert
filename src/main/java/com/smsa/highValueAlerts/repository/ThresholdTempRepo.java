package com.smsa.highValueAlerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsa.highValueAlerts.entity.SmsaThresholdTemp;

@Repository
public interface ThresholdTempRepo extends JpaRepository<SmsaThresholdTemp, Long> {
     boolean existsByUniqueCombo(
       
        String msgCurrency,
        String senderBic,
        String msgType
    );

    void deleteBySmsaThresholdId(Long smsaThresholdID);

}