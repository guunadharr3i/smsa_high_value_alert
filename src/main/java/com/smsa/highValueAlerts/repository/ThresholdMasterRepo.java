package com.smsa.highValueAlerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsa.highValueAlerts.entity.SmsaThresholdMaster;

@Repository
public interface ThresholdMasterRepo extends JpaRepository<SmsaThresholdMaster, Long> {

    boolean existsByUniqueCombo(
       
        String msgCurrency,
        String senderBic,
        String msgType
    );
}
