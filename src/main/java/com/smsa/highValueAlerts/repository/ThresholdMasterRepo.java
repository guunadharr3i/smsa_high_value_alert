package com.smsa.highValueAlerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsa.highValueAlerts.entity.SmsaThresholdMaster;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThresholdMasterRepo extends JpaRepository<SmsaThresholdMaster, Long> {

    boolean existsByMsgCurrencyAndSenderBicAndMsgType(
            String msgCurrency,
            String senderBic,
            String msgType
    );

    Optional<SmsaThresholdMaster> findByThresholdId(Long thresholdId);

    List<SmsaThresholdMaster> findByStatus(String status);
}
