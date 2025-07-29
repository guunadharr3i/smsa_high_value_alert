package com.smsa.highValueAlerts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsa.highValueAlerts.entity.SmsaThresholdTemp;
import java.util.List;

@Repository
public interface ThresholdTempRepo extends JpaRepository<SmsaThresholdTemp, Long> {

    boolean existsByMsgCurrencyAndSenderBicAndMsgType(
            String msgCurrency,
            String senderBic,
            String msgType
    );

    List<SmsaThresholdTemp> findByCreatedByNot(String createdBy);

    void deleteByThresholdId(Long smsaThresholdID);

}
